package com.xjzai1.usercenter_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjzai1.usercenter_backend.pojo.User;
import com.xjzai1.usercenter_backend.service.UserService;
import com.xjzai1.usercenter_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-10-29 14:10:15
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser() {
        User user = new User();
        user.setUsername("xjzai1");
        user.setUserAccount("2820230019");
        user.setImage("image");
        user.setGender(0);
        user.setUserPassword("ljx20041127");
        user.setPhone("15531127361");
        user.setEmail("xjzai1@gmail.com");
        userMapper.insert(user);
    }
}




