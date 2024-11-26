package com.xjzai1.usercenter_backend.mapper;

import com.xjzai1.usercenter_backend.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-10-29 14:10:15
* @Entity com.xjzai1.usercenter_backend.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




