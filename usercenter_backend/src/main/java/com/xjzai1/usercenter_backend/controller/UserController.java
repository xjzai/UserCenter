package com.xjzai1.usercenter_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xjzai1.usercenter_backend.common.BaseResponse;
import com.xjzai1.usercenter_backend.common.ErrorCode;
import com.xjzai1.usercenter_backend.common.ResultUtils;
import com.xjzai1.usercenter_backend.constant.userConstant;
import com.xjzai1.usercenter_backend.exception.BuisnessException;
import com.xjzai1.usercenter_backend.model.domain.request.UserLoginRequest;
import com.xjzai1.usercenter_backend.model.domain.request.UserRegisterRequest;
import com.xjzai1.usercenter_backend.pojo.User;
import com.xjzai1.usercenter_backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000/"},allowCredentials = "true")
public class UserController implements userConstant {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BuisnessException(ErrorCode.NULL_ERROR);
        }
        Long data = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(data);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BuisnessException(ErrorCode.NULL_ERROR);
        }
        User data = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(data);
    }
    
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);

        List<User> data = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(data);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Integer userId, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BuisnessException(ErrorCode.NO_AUTH);
        }
        if (userId <= 0) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean data = userService.removeById(userId);
        return ResultUtils.success(data);
    }

    @GetMapping("/userinfo")
    public BaseResponse<User> userInfo(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BuisnessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();

        //校验是否合法
        User user = userService.getById(userId);
        User data = userService.getSafetyUser(user);
        return ResultUtils.success(data);
    }

    @PostMapping("/logout") // 不知道为什么不用Get
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        Integer data = userService.userLogout(request);
        return ResultUtils.success(data);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchTags(@RequestParam(required = false) List<String> tagList) {
        // @RequestParam(required = false)这个表示参数可以不填，如果不写，参数为空时，前端会返回很多错误，
        //  容易泄露信息，所以尽量后端处理为空的情况，返回我们想让前端看到的报错信息。
        if (CollectionUtils.isEmpty(tagList)) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUserByTags(tagList);
        return ResultUtils.success(userList);
    }

    @PostMapping("/update")
    public BaseResponse<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            throw new BuisnessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BuisnessException(ErrorCode.NO_LOGIN);
        }
        return ResultUtils.success(userService.updateUser(user, loginUser));
    }





}
