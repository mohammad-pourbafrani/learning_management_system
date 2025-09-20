package com.lms.learning_management_system.validator.user;


import com.lms.learning_management_system.validation.user.EmailOrPhoneRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhoneRequired, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false; // Let @NotNull handle null
        }

        try {
            // Use reflection to get email and phone fields
            Method getEmail = value.getClass().getMethod("getEmail");
            Method getPhone = value.getClass().getMethod("getPhone");

            String email = (String) getEmail.invoke(value);
            String phone = (String) getPhone.invoke(value);

            boolean hasEmail = email != null && !email.isBlank();
            boolean hasPhone = phone != null && !phone.isBlank();

            return hasEmail || hasPhone;

        } catch (Exception e) {
            // If the DTO doesn't have getEmail/getPhone methods, fail validation
            return false;
        }
    }
}
