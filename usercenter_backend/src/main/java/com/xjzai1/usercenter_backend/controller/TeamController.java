package com.xjzai1.usercenter_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjzai1.usercenter_backend.common.BaseResponse;
import com.xjzai1.usercenter_backend.common.ErrorCode;
import com.xjzai1.usercenter_backend.common.ResultUtils;
import com.xjzai1.usercenter_backend.exception.BusinessException;
import com.xjzai1.usercenter_backend.model.dto.TeamQuery;
import com.xjzai1.usercenter_backend.model.request.TeamAddRequest;
import com.xjzai1.usercenter_backend.model.pojo.Team;
import com.xjzai1.usercenter_backend.model.pojo.User;
import com.xjzai1.usercenter_backend.model.request.TeamUpdateRequest;
import com.xjzai1.usercenter_backend.model.vo.TeamVo;
import com.xjzai1.usercenter_backend.service.TeamService;
import com.xjzai1.usercenter_backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000/"},allowCredentials = "true")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Integer> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        team.setUserId(loginUser.getId());
        Integer teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    // todo 还没有实现把连接表的内容一起删掉呢
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(int teamId) {
        if (teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(teamId);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest team, HttpServletRequest request) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) userService.getLoginUser(request);
        boolean result = teamService.updateTeam(team, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam(int id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/get/list")
    public BaseResponse<List<TeamVo>> getTeamList(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamVo> teamList = teamService.getTeamList(teamQuery, isAdmin);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/get/page")
    public BaseResponse<Page<Team>> getTeamPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(teamPage);
    }
}
