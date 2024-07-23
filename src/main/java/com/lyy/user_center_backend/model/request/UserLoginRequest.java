package com.lyy.user_center_backend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5342061553712510853L;

    private String userAccount; // 用户账号

    private String userPassword; // 用户密码
}
