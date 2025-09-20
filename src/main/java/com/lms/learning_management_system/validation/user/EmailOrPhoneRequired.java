package com.lms.learning_management_system.validation.user;

import com.lms.learning_management_system.validator.user.EmailOrPhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailOrPhoneValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneRequired {

    String message() default "Either phone or email must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
