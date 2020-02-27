package com.ant.ptpapp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("请求用户信息参数")
public class ReqUserInfo {
    @ApiModelProperty("微信编号")
    String wxCode;
    @ApiModelProperty("手机号码")
    String phoneNum;
    @ApiModelProperty("登录密码")
    String pwd;
    @ApiModelProperty("手机验证码")
    String validateCode;
}
