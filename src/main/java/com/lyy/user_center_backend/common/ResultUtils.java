package com.lyy.user_center_backend.common;

/**
 *返回工具类
 */
public class ResultUtils {
    public static  <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(20000,data,"操作成功");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
}
