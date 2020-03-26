package com.ant.ptpapp.handler;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
@Slf4j
public class PtpAppExceptionHandler {

    @ExceptionHandler(value = Exception.class)//指定拦截的异常
    public GenericResponse errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
        log.error("PtpAppExceptionHandler ===> ",e);
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }
}
