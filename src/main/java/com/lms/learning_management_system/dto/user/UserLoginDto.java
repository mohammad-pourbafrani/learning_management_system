package com.lms.learning_management_system.dto.user;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String phone;
    private String password;
}
