package com.lyy.user_center_backend.common;

/**
 * 错误码
 */
public enum ErrorCode {
    SUCCESS(0, "ok",""),
    PARAMS_ERROR(40000, "请求参数错误",""),
    NOT_FOUND(40400, "请求资源不存在",""),
    FORBIDDEN(40300, "无权限访问",""),
    SYSTEM_ERROR(50000, "系统内部错误",""),
    USER_NOT_LOGIN(40100, "用户未登录",""),
    USER_NOT_EXIST(40101, "用户不存在",""),
    USER_PASSWORD_ERROR(40102, "用户密码错误",""),
    USER_NOT_ACTIVE(40103, "用户未激活",""),
    USER_EMAIL_EXIST(40104, "用户邮箱","");

    private int code;

    private String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
