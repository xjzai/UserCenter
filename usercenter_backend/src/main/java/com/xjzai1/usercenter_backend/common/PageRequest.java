package com.xjzai1.usercenter_backend.common;

import lombok.Data;

import java.io.Serializable;


@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 6242530834990118851L;


    /**
     * 当前是第几页
     */
    protected int pageNum;


    /**
     * 页面大小
     */
    protected int pageSize;

}
