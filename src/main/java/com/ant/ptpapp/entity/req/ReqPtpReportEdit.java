package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-03-01 9:52 上午
 */
@Data
@ApiModel("上报上传参数")
public class ReqPtpReportEdit {
    @ApiModelProperty("上报时间 (yyyy-MM-DD HH:mm:ss)")
    String reportTime;
    @ApiModelProperty("蓝牙名称")
    String btName;
    @ApiModelProperty("上报类型 （1.进闸口 2.车厢  3.出闸口）")
    Integer reportType;

}
