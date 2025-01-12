package com.xjzai1.usercenter_backend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamUpdateRequest implements Serializable {
    private static final long serialVersionUID = -5128132592058438034L;

    /**
     * id
     */
    private Integer id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    // todo 可能会出bug，先不加这个字段
    // private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    // todo 先不加了，如果以后实现转让队长可以加
    // private Integer userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}
