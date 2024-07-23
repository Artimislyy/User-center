package com.lyy.user_center_backend.exception;

// 自定义异常类
public class CustomException extends RuntimeException{
    private final int code;
    private final String description;

    public CustomException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
}
