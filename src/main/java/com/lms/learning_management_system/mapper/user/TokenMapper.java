package com.lms.learning_management_system.mapper.user;

import com.lms.learning_management_system.dto.user.TokenDto;
import com.lms.learning_management_system.entity.user.UserTokens;

import java.time.ZoneOffset;

public class TokenMapper {
    public static TokenDto toDto(UserTokens userTokens) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(userTokens.getAccessToken());
        tokenDto.setRefreshToken(userTokens.getRefreshToken());
        tokenDto.setRefreshExpires(userTokens.getRefreshExpiresAt().toEpochSecond(ZoneOffset.UTC));
        tokenDto.setAccessExpires(userTokens.getAccessExpiresAt().toEpochSecond(ZoneOffset.UTC));
        return tokenDto;
    }
}
