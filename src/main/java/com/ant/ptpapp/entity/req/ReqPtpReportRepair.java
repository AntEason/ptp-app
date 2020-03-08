package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-03-01 6:42 下午
 */
@Data
@ApiModel("补报上传参数")
public class ReqPtpReportRepair {
    /**
     * 补报进闸口
     */
    @ApiModelProperty("补报进闸口")
    private String repairGet;

    /**
     * 补报线路
     */
    @ApiModelProperty("补报线路")
    private String repairLine;

    /**
     * 补报出闸口
     */
    @ApiModelProperty("补报出闸口")
    private String repairOut;

    /**
     * 补报出闸口
     */
    @ApiModelProperty("补充资料")
    private String remark;

}
