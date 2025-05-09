package com.lxj.myblog.handler;

import com.lxj.myblog.constant.MessageConstant;
import com.lxj.myblog.domain.response.ApiResponse;

import com.lxj.myblog.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ApiResponse exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler
    public ApiResponse exceptionHandler(SQLIntegrityConstraintViolationException ex) {
    // Duplicate entry 'lxj' for key 'employee.idx_username'
         String message =ex.getMessage();
         if(message.contains("Duplicate entry")){
            String[] split =message.split(" ");
            String username = split[2];
            String msg  = username + MessageConstant.ALREADY_EXISTS;
            return ApiResponse.error(msg);
         }
         else{
             return ApiResponse.error(MessageConstant.UNKNOWN_ERROR);
         }
    }
}
