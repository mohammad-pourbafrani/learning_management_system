package com.lms.learning_management_system.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotBlank(message = "access token is required")
    private String accessToken;

    @NotBlank(message = "refresh token is required")
    private String refreshToken;
}
