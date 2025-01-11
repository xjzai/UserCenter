package com.xjzai1.usercenter_backend.service;

import com.xjzai1.usercenter_backend.model.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


//用户插入单元测试，注意打包时要删掉或忽略，不然打一次包就插入一次
@SpringBootTest
public class InsertDataTest {
    @Resource
    private UserService userService;


    /**
     * 并发批量插入用户   100000  耗时： 26830ms
     */
    @Test
    public void insertData(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        int j = 0;
        //批量插入数据的大小
        int batchSize = 5000;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM / batchSize; i++) {
            List<User> userlist = new ArrayList<User>();
            while(true) {
                j ++;
                if (j % batchSize == 0) {
                    break;
                }
                User user = new User();
                user.setUsername("Faker");
                user.setUserAccount("faker");
                user.setImage("https://th.bing.com/th/id/R.fc7e5ed2517126f4610689774d687bbd?rik=PU6T4sOW3D49kA&riu=http%3a%2f%2fi0.hdslb.com%2fbfs%2farticle%2f11d74f7f83c65369e0a06459b56b2a6ec2ca8345.jpg&ehk=O9ddfS81%2f1o1NxJAftmHkPG3XCscbN9P1HTA%2fyvu7W4%3d&risl=&pid=ImgRaw&r=0");
                user.setGender(0);
                user.setProfile("I am the bone of my sword.Steel is my body, and fire is my blood.I have created over a thousand blades.Unknown to Death.Nor known to Life.Have withstood pain to create many weapons.Yet, those hands will never hold anything.");
                user.setUserPassword("123456");
                user.setPhone("12345678901");
                user.setEmail("faker@faker.com");
                user.setStatus(0);
                user.setUserRole(0);
                user.setTags("[\"Faker\",\"红A\"]");
                userlist.add(user);
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                System.out.println("ThreadName：" + Thread.currentThread().getName());
                userService.saveBatch(userlist,batchSize);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
