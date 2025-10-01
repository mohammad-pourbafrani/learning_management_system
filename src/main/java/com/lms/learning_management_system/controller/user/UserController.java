package com.lms.learning_management_system.controller.user;

import com.lms.learning_management_system.dto.user.*;
import com.lms.learning_management_system.service.user.UserService;
import com.lms.learning_management_system.utils.IpHandler;
import com.lms.learning_management_system.utils.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

    @PostMapping("/verify-register")
    public ResponseEntity<ApiResponse<TokenDto>> verifyRegister(@Valid @RequestBody UserVerifyRegisterDto userVerifyRegisterDto, HttpServletRequest request) {
        UserNetworkInfoDto userNetworkInfoDto = new UserNetworkInfoDto();
        userNetworkInfoDto.setIp(IpHandler.getIp(request));
        userNetworkInfoDto.setAgent(request.getHeader("User-Agent"));
        return userService.verifyRegisterUser(userVerifyRegisterDto , userNetworkInfoDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@Valid @RequestBody UserLoginDto userLoginDto , HttpServletRequest request) {
        UserNetworkInfoDto userNetworkInfoDto = new UserNetworkInfoDto();
        userNetworkInfoDto.setIp(IpHandler.getIp(request));
        userNetworkInfoDto.setAgent(request.getHeader("User-Agent"));
        return userService.login(userLoginDto , userNetworkInfoDto);
    }

    @PostMapping("/resend-otp-verify-register")
    public ResponseEntity<ApiResponse<Void>> reSendOtpVerifyRegister(@Valid @RequestBody UserOtpDtos userOtpDtos) {
        return userService.reSendOtpVerifyRegister(userOtpDtos);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        return userService.changePassword(changePasswordDto, authentication);
    }

}
