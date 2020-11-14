package com.example.authdemo.exception;

import com.example.authdemo.common.ResultStatus;



/**
 * @program: extension
 * @description: 返回结果异常类
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-09-12 13:49
 **/
public class ResultException extends RuntimeException{
    private ResultStatus resultStatus;
    private String message;

    public ResultException(ResultStatus resultStatus, String message) {
        this.resultStatus = resultStatus;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultException{" +
                "resultStatus=" + resultStatus +
                ", message='" + message + '\'' +
                '}';
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
