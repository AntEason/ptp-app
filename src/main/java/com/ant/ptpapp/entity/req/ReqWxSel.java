package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-03-02 6:52 下午
 */
@Data
@ApiModel("获取当前上报或者最近上报")
public class ReqWxSel {
    @ApiModelProperty("行程编号")
    String tripCode;
}
