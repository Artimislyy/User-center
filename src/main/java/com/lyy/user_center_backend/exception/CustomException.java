package com.lyy.user_center_backend.exception;

import com.lyy.user_center_backend.common.ErrorCode;

// 自定义异常类
public class CustomException extends RuntimeException{
    private final int code;
    private final String description;

    public CustomException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public CustomException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
