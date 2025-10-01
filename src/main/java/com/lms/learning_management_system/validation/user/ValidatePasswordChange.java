package com.lms.learning_management_system.validation.user;

import com.lms.learning_management_system.validator.user.ChangePasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChangePasswordValidator.class)
@Documented
public @interface ValidatePasswordChange {
    String message() default "Invalid password change";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}