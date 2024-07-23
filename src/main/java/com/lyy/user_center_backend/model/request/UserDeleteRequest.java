package com.lyy.user_center_backend.model.request;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserDeleteRequest implements Serializable {
    private static final long serialVersionUID = -8695575222398245006L;
    Long id;
}
