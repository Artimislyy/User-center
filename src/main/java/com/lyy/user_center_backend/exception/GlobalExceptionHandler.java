package com.lyy.user_center_backend.exception;

import com.lyy.user_center_backend.common.BaseResponse;
import com.lyy.user_center_backend.common.ErrorCode;
import com.lyy.user_center_backend.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public BaseResponse<?> customException(CustomException e){
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeException(RuntimeException e){
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage());
    }
}
