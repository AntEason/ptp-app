package com.ant.ptpapp.service;

import com.ant.ptpapp.common.GenericResponse;

import java.nio.Buffer;

/**
 * 微信业务接口
 */
public interface WeChatService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    GenericResponse wxLogin(String code)throws Exception;


    String getAccessToken();

    Buffer createQRCode(String scene);

     String jcode2Session(String code) throws Exception;
}
