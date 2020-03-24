package com.ant.ptpapp.entity.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("更新电话号码")
@Data
public class ReqUpdatePhoneNum {
    @ApiModelProperty("电话号码")
    String phoneNum;
}
