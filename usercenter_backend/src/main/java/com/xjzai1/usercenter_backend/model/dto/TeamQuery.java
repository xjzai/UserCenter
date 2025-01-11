package com.xjzai1.usercenter_backend.model.dto;

import com.xjzai1.usercenter_backend.common.PageRequest;
import lombok.Data;


@Data
public class TeamQuery extends PageRequest {
    private static final long serialVersionUID = 8374046899743061229L;
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
     * 查找关键词（队伍名称或者描述）
     */
    private String searchText;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

}
