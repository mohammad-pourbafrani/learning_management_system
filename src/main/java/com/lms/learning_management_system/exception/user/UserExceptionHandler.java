package com.lms.learning_management_system.exception.user;

import com.lms.learning_management_system.utils.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.lms.learning_management_system.controller.user")
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>(
                new ApiResponse<Void>(exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserExistException(UserExistException exception) {
        return new ResponseEntity<>(
                new ApiResponse<Void>(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

}
