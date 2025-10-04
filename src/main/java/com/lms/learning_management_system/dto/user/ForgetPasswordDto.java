package com.lms.learning_management_system.dto.user;

import com.lms.learning_management_system.validation.user.EmailOrPhoneRequired;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@EmailOrPhoneRequired
public class ForgetPasswordDto {
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^0\\d{2}[- ]?\\d{3}[- ]?\\d{4}$",
            message = "Invalid phone number format"
    )
    private String phone;
}
