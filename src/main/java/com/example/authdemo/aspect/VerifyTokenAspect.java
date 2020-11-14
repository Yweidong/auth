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
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
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
public class VerifyTokenAspect {

    @Pointcut("@annotation(com.example.authdemo.annotation.VerifyTokenAnno)")
    private void verifyTokenPointcut(){}

    @Before("verifyTokenPointcut()")
    public void doBefore() {}

    @Around("verifyTokenPointcut()")
    public void authToken(ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Field field;
        String token = null;
        Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String name = (String) enu.nextElement();
                try{
                    field= AuthDto.class.getDeclaredField(name);
                }catch (NoSuchFieldException e){
                    throw new ResultException(ResultStatus.BAD_REQUEST,"field `"+name+"` is not exists");
                }
                if(name == "token") {
                    token = request.getParameter("token");
                }

            }

            if(token != null) {
                String decrypt = DeshfuUtil.decrypt(token);
                if((System.currentTimeMillis()/1000) - Long.valueOf(decrypt) >=7200) {
                    throw new ResultException(ResultStatus.BAD_REQUEST,"token is out of date");
                }
            }else {
                throw new ResultException(ResultStatus.BAD_REQUEST,"token is missing");
            }

            joinPoint.proceed();//放行

    }


}
