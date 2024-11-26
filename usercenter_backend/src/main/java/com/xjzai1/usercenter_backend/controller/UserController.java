package com.xjzai1.usercenter_backend.controller;

import com.xjzai1.usercenter_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/add")
    public int addUser(){
        userService.addUser();
        return 200;
    }
}
