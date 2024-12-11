package com.xjzai1.usercenter_backend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -875766022365141246L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
