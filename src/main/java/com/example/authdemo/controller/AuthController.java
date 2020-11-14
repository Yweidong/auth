package com.example.authdemo.controller;

import com.example.authdemo.annotation.VerifyTokenAnno;
import com.example.authdemo.common.ResultStatus;
import com.example.authdemo.dto.AuthDto;
import com.example.authdemo.exception.ResultException;
import com.example.authdemo.utils.DeshfuUtil;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/auth/token")
    public String getToken() {
        String s = DeshfuUtil.encrypt(String.valueOf(System.currentTimeMillis()));
        return s;
    }

    @PostMapping("/test")
    @VerifyTokenAnno
    public void test(@RequestParam("token") String token) {

            System.out.println(token);


    }

}
