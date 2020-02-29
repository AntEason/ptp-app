package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 5:06 下午
 */
@Data
@ApiModel("设备轨迹查询")
public class ReqByDeviceIdSel {
    @ApiModelProperty("开始时间 (yyyy-MM-DD HH:mm:ss)")
    String startTime;
    @ApiModelProperty("结束时间 (yyyy-MM-DD HH:mm:ss)")
    String endTime;
    @ApiModelProperty("筛选条件")
    String condition;
    @ApiModelProperty("每页现实的条数")
    Integer pageSize =10;
    @ApiModelProperty("当前页数")
    Integer page=0;
    @ApiModelProperty("设备编号")
    String deviceId;
    @ApiModelProperty("设备类型（1.进闸口 2.车厢  3.出闸口）")
    String deviceType;
}
