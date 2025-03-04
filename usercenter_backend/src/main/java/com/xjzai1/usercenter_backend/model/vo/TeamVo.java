package com.xjzai1.usercenter_backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TeamVo implements Serializable {

    private static final long serialVersionUID = 6877579971811319100L;
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
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 创建人用户信息
     */
    private UserVo createUserVo;

    /**
     * 已加入用户数
     */
    private Integer hasJoinNum;

    /**
     * 是否已经加入队伍
     */
    private boolean hasJoin = false;

    /**
     * 是否是管理员 不知道为什么不能使用isAdmin字段，使用后用不了Lombok
     */
    private boolean admin = false;

    /**
     * 入队用户信息
     */
    private List<UserVo> userList;

}
