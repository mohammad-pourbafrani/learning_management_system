package com.lms.learning_management_system.controller.user;

import com.lms.learning_management_system.dto.user.TokenDto;
import com.lms.learning_management_system.dto.user.UserLoginDto;
import com.lms.learning_management_system.dto.user.UserRegisterDto;
import com.lms.learning_management_system.dto.user.UserVerifyRegisterDto;
import com.lms.learning_management_system.service.user.UserService;
import com.lms.learning_management_system.utils.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDto userRegisterDto , Authentication authentication) {
        return new ResponseEntity<>(userService.register(userRegisterDto), HttpStatus.CREATED);
    }

    @PostMapping("/verify-register")
    public ResponseEntity<ApiResponse<TokenDto>> verifyRegister(@Valid @RequestBody UserVerifyRegisterDto userVerifyRegisterDto) {
        return ResponseEntity.ok(userService.verifyRegisterUser(userVerifyRegisterDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(userService.login(userLoginDto));
    }

}
