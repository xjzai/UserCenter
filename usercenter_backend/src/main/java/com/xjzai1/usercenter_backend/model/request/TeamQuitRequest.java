package com.xjzai1.usercenter_backend.model.request;


import lombok.Data;

/**
 * 用户退出队伍请求体
 */
@Data
public class TeamQuitRequest {

    /**
     * teamid
     */
    private Integer teamId;
}
