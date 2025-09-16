package com.lms.learning_management_system.dto.user;

import com.lms.learning_management_system.utils.UserRole;
import lombok.Data;

@Data
public class UserRegisterDto {
    private String email;
    private String phone;
    private UserRole role;
    private String password;
}
