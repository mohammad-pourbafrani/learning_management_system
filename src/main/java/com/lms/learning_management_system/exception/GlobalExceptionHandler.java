package com.lms.learning_management_system.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lms.learning_management_system.utils.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Validation error");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errors));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>( exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ApiResponse<>( exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleEnumParseException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType().isEnum()) {
                Class<?> enumType = invalidFormat.getTargetType();
                String fieldName = invalidFormat.getPath().get(0).getFieldName();
                Object invalidValue = invalidFormat.getValue();
                String allowed = String.join(", ",
                        Arrays.stream(enumType.getEnumConstants())
                                .map(Object::toString)
                                .toList()
                );

                String message = String.format(
                        "Invalid value '%s' for field '%s'. Allowed values are: [%s]",
                        invalidValue, fieldName, allowed
                );

                return ResponseEntity.badRequest().body(new ApiResponse<>(message));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Invalid request payload"));
    }
}
