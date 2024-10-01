package com.Hindol.Uber.Advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIResponse<T> {
    private LocalDateTime timeStamp;
    private T data;
    private APIError apiError;

    public APIResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public APIResponse(T data) {
        this();
        this.data = data;
    }

    public APIResponse(APIError apiError) {
        this();
        this.apiError = apiError;
    }
}
