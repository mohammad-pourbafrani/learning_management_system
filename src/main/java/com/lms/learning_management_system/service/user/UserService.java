package com.lms.learning_management_system.service.user;

import com.lms.learning_management_system.dto.user.UserLoginDto;
import com.lms.learning_management_system.dto.user.UserRegisterDto;
import com.lms.learning_management_system.dto.user.TokenDto;
import com.lms.learning_management_system.dto.user.UserVerifyRegisterDto;
import com.lms.learning_management_system.entity.user.User;
import com.lms.learning_management_system.entity.user.UserTokens;
import com.lms.learning_management_system.exception.user.UserExistException;
import com.lms.learning_management_system.exception.user.UserNotFoundException;
import com.lms.learning_management_system.mapper.user.TokenMapper;
import com.lms.learning_management_system.repository.user.UserLoginHistoryRepository;
import com.lms.learning_management_system.repository.user.UserRepository;
import com.lms.learning_management_system.repository.user.UserTokensRepository;
import com.lms.learning_management_system.utils.response.ApiResponse;
import com.lms.learning_management_system.utils.jwt.JwtUtil;
import com.lms.learning_management_system.utils.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        }
        if (phone != null) {
            user = userRepository.findUserByPhone(phone);
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return user.get();
    }

    private TokenDto getToken(User user) {

        UserTokens userTokens = new UserTokens();
        String token;
        String refreshToken;

        do {
            token = jwtUtil.generateToken(user.getPhone(), user.getEmail(), user.getRole().name(), TokenType.ACCESS);
        } while (userTokensRepository.existsByAccessToken(token));

        do {
            refreshToken = jwtUtil.generateToken(user.getPhone(), user.getEmail(), user.getRole().name(), TokenType.REFRESH);
        } while (userTokensRepository.existsByRefreshToken(refreshToken));

        userTokens.setUser(user);
        userTokens.setAccessToken(token);
        userTokens.setRefreshToken(refreshToken);
        userTokens.setAccessExpiresAt(LocalDateTime.now().plusMinutes(15));
        userTokens.setRefreshExpiresAt(LocalDateTime.now().plusDays(5));
        userTokensRepository.save(userTokens);

        return TokenMapper.toDto(userTokens);
    }

    public ApiResponse<Void> register(UserRegisterDto userRegisterDto) {

        validateExistUserName(userRegisterDto.getEmail(), userRegisterDto.getPhone());

        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPhone(userRegisterDto.getPhone());
        //TODO: hash password
        user.setPassword(userRegisterDto.getPassword());
        user.setRole(userRegisterDto.getRole());
        userRepository.save(user);
        //TODO: send otp for verify user
        return new ApiResponse<Void>(HttpStatus.CREATED.value(), "user registered successfully");
    }

    public ApiResponse<TokenDto> verifyRegisterUser(UserVerifyRegisterDto userVerifyRegisterDto) {

        User user = getUser(userVerifyRegisterDto.getEmail(), userVerifyRegisterDto.getPhone());

        //TODO:check corrected otp code
        user.setEnable(true);
        userRepository.save(user);


        return new ApiResponse<TokenDto>(
                HttpStatus.OK.value(),
                "user verification registered successfully",
                getToken(user));

    }

    public ApiResponse<TokenDto> login(UserLoginDto userLoginDto) {

        User user = getUser(userLoginDto.getEmail(), userLoginDto.getPhone());

        return new ApiResponse<TokenDto>(
                HttpStatus.OK.value(),
                "successfully logged in",
                getToken(user));
    }

}
