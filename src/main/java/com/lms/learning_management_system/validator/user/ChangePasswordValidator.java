package com.lms.learning_management_system.validator.user;

import com.lms.learning_management_system.dto.user.ChangePasswordDto;
import com.lms.learning_management_system.validation.user.ValidatePasswordChange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChangePasswordValidator implements ConstraintValidator<ValidatePasswordChange , ChangePasswordDto> {

    @Override
    public boolean isValid(ChangePasswordDto changePasswordDto, ConstraintValidatorContext context) {
        if(changePasswordDto.getOldPassword() == null || changePasswordDto.getPassword() == null) {
            return true; // @NotBlank will catch null/empty
        }

        if (changePasswordDto.getOldPassword().equals(changePasswordDto.getPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("New password must not be the same as old password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }


        return true;
    }
}
