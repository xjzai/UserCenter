package com.xjzai1.usercenter_backend.service;
import java.util.Arrays;
import java.util.List;

import com.xjzai1.usercenter_backend.model.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("xjzai1");
        user.setUserAccount("2820230019");
        user.setImage("image");
        user.setGender(0);
        user.setUserPassword("ljx20041127");
        user.setPhone("15531127361");
        user.setEmail("xjzai1@gmail.com");
        boolean result = userService.save(user);
//        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

//    @Test
//    void userRegister() {
//        String userAccount = "xjzai1";
//        String userPassword = " ";
//        String checkPassword = "ljx20041127";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "xj";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "xjz1";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "xjz ai1";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//    }

    @Test
    void testSearchUserByTags() {
        List<String> tags = Arrays.asList("java", "python");
        List<User> users = userService.searchUserByTags(tags);
        System.out.println(users.toString());
        Assert.assertNotNull(users);
    }
}