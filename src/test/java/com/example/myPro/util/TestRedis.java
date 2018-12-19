package com.example.myPro.util;

import com.example.myPro.Application;
import com.example.myPro.model.database.User;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRedis {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() throws Exception {
        User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");
        stringRedisTemplate.opsForValue().set("aaa", new Gson().toJson(user), 5, TimeUnit.SECONDS);
//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }


    @Test
    public void testObj() throws Exception {
        User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        operations.set("com.neo.x", user);
        operations.set("com.neo.f", user, 10000, TimeUnit.SECONDS);
        Thread.sleep(1000);
//        redisTemplate.delete("com.neo.f");
        User user1 = (User) redisTemplate.opsForValue().get("com.neo.x");
        boolean exists = redisTemplate.hasKey("com.neo.f");
        if (exists) {
            System.out.println("exists is true");
        } else {
            System.out.println("exists is false");
        }
        Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }

    @Test
    public void testRedisUtil() throws Exception {
        User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");
        redisUtil.setEx("aaa", new Gson().toJson(user), 10000, TimeUnit.SECONDS);
    }
}
