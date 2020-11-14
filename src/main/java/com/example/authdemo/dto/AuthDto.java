package com.example.authdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NonNull;

/**
 * @program: auth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-13 13:55
 **/
@Data
public class AuthDto {


    private String token;
}
