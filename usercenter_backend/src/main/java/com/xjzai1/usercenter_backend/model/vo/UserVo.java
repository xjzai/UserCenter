package com.xjzai1.usercenter_backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 2866258176111054445L;
    /**
     *
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 头像链接
     */
    private String image;

    /**
     * 0-男 1-女
     */
    private Integer gender;

    /**
     * 个人简介
     */
    private String profile;


    /**
     *
     */
    private String phone;

    /**
     *
     */
    private String email;

    /**
     * 用户状态 0-正常 1-封号
     */
    private Integer status;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;


    /**
     * 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 标签
     */
    private String tags;

}
