package com.xjzai1.usercenter_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjzai1.usercenter_backend.common.BaseResponse;
import com.xjzai1.usercenter_backend.pojo.Team;
import com.xjzai1.usercenter_backend.pojo.User;

/**
* @author Administrator
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-01-11 01:18:52
*/
public interface TeamService extends IService<Team> {

    Integer addTeam(Team team, User loginUser);
}
