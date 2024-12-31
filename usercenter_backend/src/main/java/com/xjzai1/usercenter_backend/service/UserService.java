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

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户退出
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     * @param tags
     * @return
     */
    List<User> searchUserByTags(List<String> tags);

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return
     */
    User updateUser(User user, User loginUser);

    // 辅助函数

    /**
     * 脱敏成安全的信息用于返回
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 获取当前登录用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param user
     * @return
     */
    boolean isAdmin(User user);



}
