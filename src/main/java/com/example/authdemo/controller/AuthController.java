package com.example.authdemo.controller;

import com.example.authdemo.annotation.IdempotentTokenAnno;

import com.example.authdemo.exception.ResultException;
import com.example.authdemo.utils.DeshfuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @program: auth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-13 13:33
 **/
@RestController
@RequestMapping("api/v1")

public class AuthController {
    private static final String REDIS_KEY = "token";
    private static final long TIME_STAMP = 60*60;
    @Autowired
    RedisTemplate redisTemplate;
    @GetMapping("/auth/token")
    public String getToken() {
        String s = DeshfuUtil.encrypt(String.valueOf(System.currentTimeMillis()));
        redisTemplate.opsForValue().setIfAbsent(REDIS_KEY,s,TIME_STAMP, TimeUnit.SECONDS);
        return s;
    }

    @PostMapping("/test")
    @IdempotentTokenAnno
    public void test(HttpServletRequest request) {

            System.out.println("111");


    }

}
