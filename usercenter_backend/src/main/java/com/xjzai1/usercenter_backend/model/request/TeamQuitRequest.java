package com.xjzai1.usercenter_backend.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -3467965127109505225L;
    /**
     * teamid
     */
    private Integer teamId;
}
