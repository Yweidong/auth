package com.example.authdemo.aspect;

import com.example.authdemo.common.ResultStatus;
import com.example.authdemo.dto.AuthDto;
import com.example.authdemo.exception.ResultException;
import com.example.authdemo.utils.DeshfuUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: auth
 * @description: 校验token
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-13 11:35
 **/
@Aspect
@Component
@Slf4j
public class IdempotentTokenAspect {

    @Autowired
    RedisTemplate redisTemplate;
    private static final String TOKEN_NAME = "token";
    @Pointcut("@annotation(com.example.authdemo.annotation.IdempotentTokenAnno)")
    private void verifyTokenPointcut(){}

    @Before("verifyTokenPointcut()")
    public void doBefore() {

    }

    @Around("verifyTokenPointcut()")
    public void authToken(ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader("token");//请求头中是否有token
        if(StringUtils.isEmpty(token)) {
            token = request.getParameter(TOKEN_NAME);
            if(StringUtils.isEmpty(token)) {
                throw new ResultException(ResultStatus.BAD_REQUEST,"请勿重复提交1");
            }
        }
        if(!redisTemplate.hasKey(TOKEN_NAME)) {
            throw new ResultException(ResultStatus.BAD_REQUEST,"请勿重复提交2");
        }
//        System.out.println(token);
        if(!redisTemplate.opsForValue().get(TOKEN_NAME).equals(token)) {
            throw new ResultException(ResultStatus.BAD_REQUEST,"请勿重复提交3");
        }

        Boolean delete = redisTemplate.delete(TOKEN_NAME);
        if(!delete) {
            throw new ResultException(ResultStatus.BAD_REQUEST,"请勿重复提交4");
        }

        joinPoint.proceed();//放行

    }


}
