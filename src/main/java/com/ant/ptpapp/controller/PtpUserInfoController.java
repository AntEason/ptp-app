package com.ant.ptpapp.controller;


import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.ReqUserInfo;
import com.ant.ptpapp.service.WeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */


@Api(value="用户信息",tags={"用户操作接口"})
@RestController
@Slf4j
public class PtpUserInfoController {

    @Autowired
    private WeChatService weChatService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",tags={"通过微信编号登陆"})
    public GenericResponse login( @RequestBody ReqUserInfo reqUserInfo)throws Exception{
        log.info(reqUserInfo.getWxCode());
        return weChatService.wxLogin(reqUserInfo.getWxCode());
    }


}

