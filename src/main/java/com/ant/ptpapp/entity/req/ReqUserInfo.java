package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("请求用户信息参数")
public class ReqUserInfo {
    @ApiModelProperty("微信编号")
    String wxCode;
    @ApiModelProperty("用户信息的加密数据")
    String encryptedData;
    @ApiModelProperty("加密算法的初始向量")
    String iv;
    @ApiModelProperty("手机号码")
    String userPhone;
    @ApiModelProperty("登录密码")
    String userPwd;
    @ApiModelProperty("手机验证码")
    String validateCode;
}
