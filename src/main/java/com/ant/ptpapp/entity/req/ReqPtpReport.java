package com.ant.ptpapp.entity.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 3:36 下午
 */
@Data
@ApiModel("上报查询请求参数")
public class ReqPtpReport {
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
    @ApiModelProperty("用户编号")
    String userId;
    @ApiModelProperty("用户手机号")
    String userPhone;
}
