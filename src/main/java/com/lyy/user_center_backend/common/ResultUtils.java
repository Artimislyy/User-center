package com.lyy.user_center_backend.common;

/**
 *返回工具类
 */
public class ResultUtils {
    public static  <T> BaseResponse<T> success(T data,String description){
        return new BaseResponse<>(20000,data,"操作成功",description);
    }
    public static  <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(20000,data,"操作成功");
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse(errorCode, message);
    }

}
