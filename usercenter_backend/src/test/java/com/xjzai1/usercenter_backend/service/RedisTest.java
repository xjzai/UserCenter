package com.xjzai1.usercenter_backend.service;

import com.xjzai1.usercenter_backend.model.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 增
        valueOperations.set("xjzai1String", "xjzai1");
        valueOperations.set("xjzai1Integer", 1);
        valueOperations.set("xjzai1Double", 2.0);
        User user = new User();
        user.setId(1);
        user.setUsername("xjzai1");
        valueOperations.set("xjzai1User", user);

        // 查
        Object xjzai1 = valueOperations.get("xjzai1String");
        Assertions.assertTrue("xjzai1".equals((String) xjzai1));
        xjzai1 = valueOperations.get("xjzai1Integer");
        Assertions.assertTrue(1 == (Integer) xjzai1);
        xjzai1 = valueOperations.get("xjzai1Double");
        Assertions.assertTrue(2.0 == (Double) xjzai1);
        System.out.println(valueOperations.get("xjzai1User"));
//        valueOperations.set("xjzai1String", "xjzai1");


    }
}
