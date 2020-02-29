package com.ant.ptpapp.controller;

import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class TestController {

    @Autowired
    private WeChatService weChatService;
    /**
     * 权限测试
     */
    @GetMapping("/hello")
    public GenericResponse hello() throws UnsupportedEncodingException {
        weChatService.createQRCode("123");
        return GenericResponse.response(ServiceError.NORMAL,"hello security");
    }


}
