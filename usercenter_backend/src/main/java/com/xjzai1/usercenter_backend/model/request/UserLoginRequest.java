package com.xjzai1.usercenter_backend.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = 6532334309161725757L;

    private String userAccount;
    private String userPassword;
}
