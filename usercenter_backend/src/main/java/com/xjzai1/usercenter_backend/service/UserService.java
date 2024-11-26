package com.xjzai1.usercenter_backend.service;

import com.xjzai1.usercenter_backend.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-10-29 14:10:15
*/
public interface UserService extends IService<User> {

    public void addUser();

}
