package com.xjzai1.usercenter_backend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjzai1.usercenter_backend.pojo.User;
import com.xjzai1.usercenter_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedissonClient redissonClient;

    // 重点用户（就是给这些用户每天更新缓存）
    private List<Integer> mainUserList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 37 * * * *") // 每次使用时需要自己重新设置时间 https://cron.qqe2.com/
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("xjzai1:precachejob:docache:lock");

        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)){ // 看门狗机制，参数2必须设置为-1，原理：
                // 1. 监听当前线程，默认过期时间是 30 秒，每 10 秒续期一次（补到 30 秒）
                // 2. 如果线程挂掉（注意 debug 模式也会被它当成服务器宕机），则不会续期
//                Thread.sleep(300000);
                System.out.println("getLock: " + Thread.currentThread().getId());
                for (Integer userid : mainUserList) {
                    //查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
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
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
