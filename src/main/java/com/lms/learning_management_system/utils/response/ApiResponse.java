package com.lms.learning_management_system.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonInclude(JsonInclude.Include.NON_NULL)  // 👈 ignore null fields
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private Long time;
    private T data;

    //convenience constructor success with data
    public ApiResponse(String message, T data) {
        this.message = message;
        this.time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.data = data;
    }

    //convenience constructor success without data
    public ApiResponse(String message) {
        this.message = message;
        this.time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

}
