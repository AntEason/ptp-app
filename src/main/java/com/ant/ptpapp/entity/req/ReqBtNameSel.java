package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-03-01 4:26 下午
 */
@Data
@ApiModel("通过蓝牙名称获取设备信息")
public class ReqBtNameSel {
    @ApiModelProperty("系统设备编号")
    String btName;
}
