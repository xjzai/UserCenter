package com.xjzai1.usercenter_backend.mapper;

import com.xjzai1.usercenter_backend.model.pojo.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【team(队伍)】的数据库操作Mapper
* @createDate 2025-01-11 01:18:52
* @Entity com.xjzai1.usercenter_backend.pojo.Team
*/
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

}




