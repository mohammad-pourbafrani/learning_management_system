package com.lms.learning_management_system.service.user;

import com.lms.learning_management_system.dto.user.*;
import com.lms.learning_management_system.entity.user.User;
import com.lms.learning_management_system.entity.user.UserLoginHistory;
import com.lms.learning_management_system.entity.user.UserTokens;
import com.lms.learning_management_system.exception.user.UserExistException;
import com.lms.learning_management_system.exception.user.UserNotFoundException;
import com.lms.learning_management_system.mapper.user.TokenMapper;
import com.lms.learning_management_system.service.RedisService;
import com.lms.learning_management_system.repository.user.UserLoginHistoryRepository;
import com.lms.learning_management_system.repository.user.UserRepository;
import com.lms.learning_management_system.repository.user.UserTokensRepository;
import com.lms.learning_management_system.utils.otp.OtpUtils;
import com.lms.learning_management_system.utils.response.ApiResponse;
import com.lms.learning_management_system.utils.jwt.JwtUtil;
import com.lms.learning_management_system.utils.jwt.TokenType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserTokensRepository userTokensRepository;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final OtpUtils otpUtils;
    private final PasswordEncoder passwordEncoder;
    private final String REGISTER_PREFIX = "register";
    private final String FORGET_PASSWORD_PREFIX = "register";

    private void validateExistUserName(String email, String phone) {
        if (email != null && userRepository.existsByEmail(email)) {
            throw new UserExistException("User with this Email already exist");
        }
        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new UserExistException("User with this Phone already exist");
        }
    }

    private User getUser(String email, String phone) {

        Optional<User> user = Optional.empty();

        if (email != null) {
            user = userRepository.findUserByEmail(email);
        } else if (phone != null) {
            user = userRepository.findUserByPhone(phone);
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return user.get();
    }

    private TokenDto getToken(User user, UserNetworkInfoDto userNetworkInfoDto) {

        String token;
        String refreshToken;

        do {
            token = jwtUtil.generateToken(user.getPhone(), user.getEmail(), user.getRole().name(), TokenType.ACCESS);
        } while (userTokensRepository.existsByAccessToken(token));

        do {
            refreshToken = jwtUtil.generateToken(user.getPhone(), user.getEmail(), user.getRole().name(), TokenType.REFRESH);
        } while (userTokensRepository.existsByRefreshToken(refreshToken));

        //delete all exist tokens
        userTokensRepository.deleteAllByUser(user);

        //create new token
        UserTokens userTokens = new UserTokens();
        userTokens.setUser(user);
        userTokens.setAccessToken(token);
        userTokens.setRefreshToken(refreshToken);
        userTokens.setAccessExpiresAt(LocalDateTime.now().plusMinutes(15));
        userTokens.setRefreshExpiresAt(LocalDateTime.now().plusDays(5));
        userTokensRepository.save(userTokens);

        //insert in login history
        UserLoginHistory userLoginHistory = new UserLoginHistory();
        userLoginHistory.setUser(user);
        userLoginHistory.setIpAddress(userNetworkInfoDto.getIp());
        userLoginHistory.setUserAgent(userNetworkInfoDto.getAgent());
        userLoginHistoryRepository.save(userLoginHistory);

        return TokenMapper.toDto(userTokens);
    }

    private String buildOtpKey(String prefix, String email, String phone) {
        return prefix + (!email.isBlank() ? email : phone);
    }

    public ResponseEntity<ApiResponse<Void>> register(UserRegisterDto userRegisterDto) {

        validateExistUserName(userRegisterDto.getEmail(), userRegisterDto.getPhone());

        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPhone(userRegisterDto.getPhone());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRole(userRegisterDto.getRole());
        userRepository.save(user);

        String otp = otpUtils.generateRegisterOtp();
        redisService.setValue(
                buildOtpKey(REGISTER_PREFIX, userRegisterDto.getEmail(), userRegisterDto.getPhone()),
                otp,
                300);

        //TODO: sendOtp
        System.out.println("ottttttttp: " + otp);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ApiResponse<>("user registered successfully"));
    }

    @Transactional
    public ResponseEntity<ApiResponse<TokenDto>> verifyRegisterUser(UserVerifyRegisterDto userVerifyRegisterDto, UserNetworkInfoDto userNetworkInfoDto) {

        User user = getUser(userVerifyRegisterDto.getEmail(), userVerifyRegisterDto.getPhone());

        String storedOtp = redisService.getValue(buildOtpKey(REGISTER_PREFIX, userVerifyRegisterDto.getEmail(), userVerifyRegisterDto.getPhone()));

        if (user.isEnable()) {
            throw new UserExistException("user recently verified");
        }

        if (storedOtp != null && storedOtp.equalsIgnoreCase(userVerifyRegisterDto.getCode())) {
            user.setEnable(true);
            userRepository.save(user);

            redisService.delete(buildOtpKey(REGISTER_PREFIX, userVerifyRegisterDto.getEmail(), userVerifyRegisterDto.getPhone()));
            return ResponseEntity.ok(new ApiResponse<>(
                    "user verification registered successfully",
                    getToken(user, userNetworkInfoDto)));
        }

        return ResponseEntity.badRequest().body(new ApiResponse<>("otp code not found or expire"));


    }

    @Transactional
    public ResponseEntity<ApiResponse<TokenDto>> login(UserLoginDto userLoginDto, UserNetworkInfoDto userNetworkInfoDto) {

        User user = getUser(userLoginDto.getEmail(), userLoginDto.getPhone());

        if (!user.isEnable()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("User not enabled"));
        }
        if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(new ApiResponse<>(
                    "successfully logged in",
                    getToken(user, userNetworkInfoDto)));
        }

        return ResponseEntity.badRequest().body(new ApiResponse<>("password not correct"));

    }

    public ResponseEntity<ApiResponse<Void>> reSendOtpVerifyRegister(UserOtpDtos userOtpDtos) {

        User user = getUser(userOtpDtos.getEmail(), userOtpDtos.getPhone());

        String storedOtp = redisService.getValue(buildOtpKey(REGISTER_PREFIX, user.getEmail(), user.getPhone()));

        if (user.isEnable()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("user verification already done"));
        }

        if (storedOtp == null) {
            String otp = otpUtils.generateRegisterOtp();
            redisService.setValue(
                    buildOtpKey(REGISTER_PREFIX, userOtpDtos.getEmail(), userOtpDtos.getPhone()),
                    otp,
                    300);
            //TODO: sendOtp
            System.out.println("ottttttttp: " + otp);

            return ResponseEntity.ok().body(new ApiResponse<>("otp send again successfully"));
        }

        //TODO: sendOtp
        System.out.println("ottttttttp: " + storedOtp);
        return ResponseEntity.ok().body(new ApiResponse<>("otp send again successfully"));
    }


    @Transactional
    public ResponseEntity<ApiResponse<Void>> changePassword(ChangePasswordDto changePasswordDto, Authentication authentication) {

        User user = getUser(authentication.getName(), authentication.getName());

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Old passwords don't match"));
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
        userRepository.save(user);

        userTokensRepository.deleteAllByUser(user);

        return ResponseEntity.ok().body(new ApiResponse<>("password changed successfully , login again"));
    }

    public ResponseEntity<ApiResponse<Void>> forgetPassword(ForgetPasswordDto forgetPasswordDto) {
        User user = getUser(forgetPasswordDto.getEmail(), forgetPasswordDto.getPhone());

        String storedOtp = redisService.getValue(buildOtpKey(FORGET_PASSWORD_PREFIX, user.getEmail(), user.getPhone()));

        if (storedOtp == null) {
            String otp = otpUtils.generateForgetPasswordOtp();
            redisService.setValue(
                    buildOtpKey(REGISTER_PREFIX, forgetPasswordDto.getEmail(), forgetPasswordDto.getPhone()),
                    otp,
                    300);
            //TODO: sendOtp
            System.out.println("ottttttttp: " + otp);

            return ResponseEntity.ok().body(new ApiResponse<>("otp send successfully"));
        }

        //TODO: sendOtp
        System.out.println("ottttttttp: " + storedOtp);
        return ResponseEntity.ok().body(new ApiResponse<>("otp send again successfully"));
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> verifyForgetPassword(VerifyForgetPasswordDto verifyForgetPasswordDto) {
        User user = getUser(verifyForgetPasswordDto.getEmail(), verifyForgetPasswordDto.getPhone());

        String storedOtp = redisService.getValue(buildOtpKey(FORGET_PASSWORD_PREFIX, user.getEmail(), user.getPhone()));

        if (storedOtp != null && storedOtp.equalsIgnoreCase(verifyForgetPasswordDto.getCode())) {
            redisService.delete(buildOtpKey(FORGET_PASSWORD_PREFIX, user.getEmail(), user.getPhone()));
            user.setPassword(passwordEncoder.encode(verifyForgetPasswordDto.getPassword()));
            userRepository.save(user);
            userTokensRepository.deleteAllByUser(user);
            return ResponseEntity.ok().body(new ApiResponse<>("password changed successfully"));
        }

        return ResponseEntity.badRequest().body(new ApiResponse<>("code not found or expire verify code"));
    }

    @Transactional
    public ResponseEntity<ApiResponse<TokenDto>> refreshToken(RefreshTokenDto refreshTokenDto, UserNetworkInfoDto userNetworkInfoDto) {
        Optional<UserTokens> token = userTokensRepository.findByAccessToken(refreshTokenDto.getAccessToken());
        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("access token not found"));
        }
        if (token.get().getRefreshToken().equals(refreshTokenDto.getRefreshToken())) {
            return ResponseEntity.ok().body(new ApiResponse<>("tokens is refresh successfully", getToken(token.get().getUser(), userNetworkInfoDto)));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>("refresh token not valid"));
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> logoutUser(Authentication authentication) {
        User user = getUser(authentication.getName(), authentication.getName());
        userTokensRepository.deleteAllByUser(user);
        return ResponseEntity.ok().body(new ApiResponse<>("logged out successfully"));
    }

}
