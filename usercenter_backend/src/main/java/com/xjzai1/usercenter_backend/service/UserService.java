package com.xjzai1.usercenter_backend.service;

import com.xjzai1.usercenter_backend.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-10-29 14:10:15
*/
public interface UserService extends IService<User> {

    public long userRegister(String userAccount, String userPassword, String checkPassword);
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request);
    public int userLogout(HttpServletRequest request);


    List<User> searchUserByTags(List<String> tags);

    User getSafetyUser(User originUser);
}
