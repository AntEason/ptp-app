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

    boolean createQRCode(String scene,String fileName);

    String jcode2Session(String code) throws Exception;
}
