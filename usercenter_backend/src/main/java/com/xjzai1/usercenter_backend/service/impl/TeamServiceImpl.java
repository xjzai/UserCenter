package com.xjzai1.usercenter_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjzai1.usercenter_backend.common.ErrorCode;
import com.xjzai1.usercenter_backend.exception.BusinessException;
import com.xjzai1.usercenter_backend.model.dto.TeamQuery;
import com.xjzai1.usercenter_backend.model.enums.TeamStatusEnum;
import com.xjzai1.usercenter_backend.model.pojo.Team;
import com.xjzai1.usercenter_backend.model.pojo.User;
import com.xjzai1.usercenter_backend.model.pojo.UserTeam;
import com.xjzai1.usercenter_backend.model.request.TeamDeleteRequest;
import com.xjzai1.usercenter_backend.model.request.TeamJoinRequest;
import com.xjzai1.usercenter_backend.model.request.TeamQuitRequest;
import com.xjzai1.usercenter_backend.model.request.TeamUpdateRequest;
import com.xjzai1.usercenter_backend.model.vo.TeamVo;
import com.xjzai1.usercenter_backend.model.vo.UserVo;
import com.xjzai1.usercenter_backend.service.TeamService;
import com.xjzai1.usercenter_backend.mapper.TeamMapper;
import com.xjzai1.usercenter_backend.service.UserService;
import com.xjzai1.usercenter_backend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private UserService userService;

    @Autowired
    private UserTeamService userTeamService;



    @Override
    // 对数据库有多个增删改操作时一定要添加，防止执行了第一个操作之后程序挂掉，导致数据库混乱
    @Transactional(rollbackFor = Exception.class)
    public Integer addTeam(Team team, User loginUser) {
//1. 请求参数是否为空？ 2. 是否登录，未登录不允许创建
        if (team == null || loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final int userId = loginUser.getId();
        //3. 校验信息
        //  a. 队伍人数 > 1 且 <= 20
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不符合要求");
        }
        //  b. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //  c. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //  d. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //  e. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        // todo 不管是不是加密状态都会存入密码
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //  f. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        // todo 用户没填时间就永不过期，以后做
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
        //  g. 校验用户最多创建 5 个队伍
        // todo 有 bug，可能同时创建 100 个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        long hasTeamNum = teamMapper.selectCount(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建 5 个队伍");
        }
        //4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Integer teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        //5. 插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return userId;
    }

    @Override
    public List<TeamVo> getTeamList(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 组合查询条件 如果为空，默认查找所有，所以不需要报错
        if (teamQuery != null) {
            Integer id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            // todo 没搞懂，先注释了 因为上面是拿的鱼皮的vo,所以这里需要添加
//            List<Long> idList = teamQuery.getIdList();
//            if (CollectionUtils.isNotEmpty(idList)) {
//                queryWrapper.in("id", idList);
//            }
            List<Integer> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)) {
                queryWrapper.in("id", idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            // 查询最大人数相等的
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("max_num", maxNum);
            }
            Integer userId = teamQuery.getUserId();
            // 根据创建人来查询
            if (userId != null && userId > 0) {
                queryWrapper.eq("user_id", userId);
            }
            // 根据状态来查询
            // todo 什么都没传不应该默认是0， 应该能看所有队伍
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        // 不展示已过期的队伍
        // expireTime is null or expireTime > now()
        queryWrapper.and(qw -> qw.gt("expire_time", new Date()).or().isNull("expire_time"));
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }

        List<TeamVo> teamVoList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            Integer userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamVo teamVo = new TeamVo();
            BeanUtils.copyProperties(team, teamVo);
            // 脱敏用户信息
            if (user != null) {
                UserVo userVO = new UserVo();
                BeanUtils.copyProperties(user, userVO);
                teamVo.setCreateUserVo(userVO);
            }
            teamVoList.add(teamVo);
        }
        return teamVoList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest team, User loginUser) {
        // 判断请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer teamId = team.getId();
        if (teamId <= 0 || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询队伍是否存在
        Team oldTeam = this.getById(team.getId());
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 只有管理员或者队伍的创建者可以修改
        if (!loginUser.getId().equals(oldTeam.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        // todo 如果用户传入的新值和老值一致，就不用 update 了（可自行实现，降低数据库使用次数）
        // 如果队伍状态改为加密，必须要有密码
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(team.getStatus());
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要设置密码");
            }
        }
        // 更新成功
        Team updateTeam = new Team();
        BeanUtils.copyProperties(team, updateTeam);
        return this.updateById(updateTeam);
    }



    // todo 添加锁，防止同一时间多次加入队伍，后续优化
    @Override
    public Boolean joinTeam(TeamJoinRequest team, User loginUser) {
        // 其他人、未满、未过期，允许加入多个队伍，但是要有个上限 P0
        //1. 用户最多加入 5 个队伍
        //2. 队伍必须存在，只能加入未满、未过期的队伍
        //3. 不能加入自己的队伍，不能重复加入已加入的队伍（幂等性）
        //4. 禁止加入私有的队伍
        //5. 如果加入的队伍是加密的，必须密码匹配才可以
        //6. 新增队伍 - 用户关联信息
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer teamId = team.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team joinTeam = this.getById(teamId);
        if (joinTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        Date expireTime = joinTeam.getExpireTime();
        if (expireTime == null || expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = joinTeam.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "禁止加入私有队伍");
        }
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(joinTeam.getPassword())) {
                throw new BusinessException(ErrorCode.NO_AUTH, "密码错误");
            }
        }
        //该用户已加入的队伍数量
        Integer userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("user_id",userId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinNum > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建和加入5个队伍");
        }
        //不能重复加入已加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("user_id",userId);
        userTeamQueryWrapper.eq("team_id",teamId);
        long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
        if (hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已加入该队伍");
        }
        //已加入队伍的人数
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id",teamId);
        long teamHasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (teamHasJoinNum >= joinTeam.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已满");
        }
        //加入，修改队伍信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest team, User loginUser) {
        //1.  校验请求参数
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer teamId = team.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.  校验队伍是否存在
        Team quitTeam = this.getById(teamId);
        if (quitTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        //3.  校验我是否已加入队伍
        Integer userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id",teamId);
        long hasTeamJoinNum = userTeamService.count(userTeamQueryWrapper);
        userTeamQueryWrapper.eq("user_id",userId);
        long hasQuitTeam = userTeamService.count(userTeamQueryWrapper);
        if (hasQuitTeam <= 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户未加入该队伍");
        }
        //4.  如果队伍
        //  a.  只剩一人，队伍解散
        if (hasTeamJoinNum == 1) {
            // 解散队伍
            this.removeById(teamId);
        } else {
            //  b.  还有其他人
            //    ⅰ.  如果是队长退出队伍，权限转移给第二早加入的用户 —— 先来后到
            //只用取 id 最小的 2 条数据
            // todo 之后改成可以指定转给谁
            if (userId.equals(quitTeam.getUserId())) {
                QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("team_id", teamId);
                queryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Integer nextTeamLeaderId = nextUserTeam.getUserId();
                // 更新当前队伍的队长
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍队长失败");
                }
            }

        }
        return userTeamService.remove(userTeamQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(TeamDeleteRequest team, User loginUser) {
        //1.  校验请求参数
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer teamId = team.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.  校验队伍是否存在
        Team deleteTeam = this.getById(teamId);
        if (deleteTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 校验你是不是队伍的队长，或者是管理员
        if (!deleteTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        // 移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id", teamId);
        boolean result = userTeamService.remove(userTeamQueryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
        }
        // 删除队伍
        return this.removeById(teamId);
    }
}




