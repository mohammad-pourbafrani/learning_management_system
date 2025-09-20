package com.lms.learning_management_system.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private LocalDateTime time;
    private T data;

    //convenience constructor success with data
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.time = LocalDateTime.now();
        this.data = data;
    }

    //convenience constructor success without data
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
