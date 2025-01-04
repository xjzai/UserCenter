package com.xjzai1.usercenter_backend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjzai1.usercenter_backend.pojo.User;
import com.xjzai1.usercenter_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    // 重点用户（就是给这些用户每天更新缓存）
    private List<Integer> mainUserList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 33 * * * *") // 每次使用时需要自己重新设置时间 https://cron.qqe2.com/
    public void doCacheRecommendUser() {
        //查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
        for (Integer userid : mainUserList) {
            String redisKey = String.format("xjzai1:user:recommend:%s", userid);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            //写缓存,300s过期
            try {
                valueOperations.set(redisKey,userPage,300000, TimeUnit.MILLISECONDS);
            } catch (Exception e){
                log.error("redis set key error",e);
            }
        }

    }
}
