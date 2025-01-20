package com.xjzai1.usercenter_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xjzai1.usercenter_backend.model.dto.TeamQuery;
import com.xjzai1.usercenter_backend.model.pojo.Team;
import com.xjzai1.usercenter_backend.model.pojo.User;
import com.xjzai1.usercenter_backend.model.request.TeamDeleteRequest;
import com.xjzai1.usercenter_backend.model.request.TeamJoinRequest;
import com.xjzai1.usercenter_backend.model.request.TeamQuitRequest;
import com.xjzai1.usercenter_backend.model.request.TeamUpdateRequest;
import com.xjzai1.usercenter_backend.model.vo.TeamVo;

import java.util.List;

/**
* @author Administrator
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-01-11 01:18:52
*/
public interface TeamService extends IService<Team> {

    Integer addTeam(Team team, User loginUser);

//    List<TeamVo> getTeamList(TeamQuery teamQuery, boolean isAdmin);

    List<TeamVo> getTeamList(TeamQuery teamQuery, User loginUser);

    boolean updateTeam(TeamUpdateRequest team, User loginUser);

    Boolean joinTeam(TeamJoinRequest team, User loginUser);

    boolean quitTeam(TeamQuitRequest team, User loginUser);

    boolean deleteTeam(TeamDeleteRequest team, User loginUser);
}
