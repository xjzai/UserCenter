package com.xjzai1.usercenter_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjzai1.usercenter_backend.common.ErrorCode;
import com.xjzai1.usercenter_backend.exception.BusinessException;
import com.xjzai1.usercenter_backend.model.domain.enums.TeamStatusEnum;
import com.xjzai1.usercenter_backend.pojo.Team;
import com.xjzai1.usercenter_backend.pojo.User;
import com.xjzai1.usercenter_backend.pojo.UserTeam;
import com.xjzai1.usercenter_backend.service.TeamService;
import com.xjzai1.usercenter_backend.mapper.TeamMapper;
import com.xjzai1.usercenter_backend.service.UserService;
import com.xjzai1.usercenter_backend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
* @author Administrator
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-01-11 01:18:52
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UserTeamService userTeamService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addTeam(Team team, User loginUser) {

        return null;
    }
}




