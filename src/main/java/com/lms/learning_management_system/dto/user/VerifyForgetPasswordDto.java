package com.lms.learning_management_system.dto.user;

import com.lms.learning_management_system.validation.user.EmailOrPhoneRequired;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@EmailOrPhoneRequired
public class VerifyForgetPasswordDto {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^0\\d{2}[- ]?\\d{3}[- ]?\\d{4}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;

    @NotBlank(message = "verify code is required")
    private String code;
}
