package com.xjzai1.usercenter_backend.mapper;

import com.xjzai1.usercenter_backend.pojo.UserTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
* @createDate 2025-01-11 01:29:41
* @Entity com.xjzai1.usercenter_backend.pojo.UserTeam
*/
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeam> {

}




