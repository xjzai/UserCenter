package com.xjzai1.usercenter_backend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户加入队伍请求体
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 7092261091668254877L;

    /**
     * teamid
     */
    private Integer teamId;


    /**
     * 密码
     */
    private String password;
}
