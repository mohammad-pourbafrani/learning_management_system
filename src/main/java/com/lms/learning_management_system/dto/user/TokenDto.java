package com.lms.learning_management_system.dto.user;

import lombok.Data;

@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Long accessExpires;
    private Long refreshExpires;
}
