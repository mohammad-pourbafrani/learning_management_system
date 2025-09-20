package com.lms.learning_management_system.dto.user;

import com.lms.learning_management_system.utils.UserRole;
import com.lms.learning_management_system.validation.user.EmailOrPhoneRequired;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@EmailOrPhoneRequired
public class UserRegisterDto {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^0\\d{2}[- ]?\\d{3}[- ]?\\d{4}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;
}
