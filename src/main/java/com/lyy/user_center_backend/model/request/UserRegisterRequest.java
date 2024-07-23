package com.lyy.user_center_backend.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3564577235541091632L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
