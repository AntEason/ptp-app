package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 10:39 上午
 */
@Data
@ApiModel("绑定（手机号码、用户名称）请求参数")
public class ReqGetPhoneNum {
    @ApiModelProperty(value = "用户信息的加密数据",required = true)
    String encryptedData;
    @ApiModelProperty(value = "加密算法的初始向量",required = true)
    String iv;
    @ApiModelProperty(value = "用户名称")
    String userName;
}
