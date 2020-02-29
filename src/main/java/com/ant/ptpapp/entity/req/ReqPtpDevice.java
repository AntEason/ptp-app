package com.ant.ptpapp.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-28 11:10 上午
 */
@Data
@ApiModel("请求设备信息参数")
public class ReqPtpDevice {
    @ApiModelProperty("设备唯一id（修改必传）")
    private String deviceId;

    /**
     * 蓝牙名称
     */
    @ApiModelProperty("蓝牙名称")
    private String deviceBtName;

    /**
     * 安装时间
     */
    @ApiModelProperty("安装时间")
    private String  installationTime;

    /**
     * 1.进闸口 2.车厢  3.出闸口
     */
    @ApiModelProperty("1.进闸口 2.车厢  3.出闸口")
    private Integer deviceType;

    /**
     * 设备安装地址
     */
    @ApiModelProperty("设备安装地址")
    private String deviceAddrese;

    /**
     * 设备线路
     */
    @ApiModelProperty("设备线路")
    private Integer deviceLine;

    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    private Integer editDeviceId;
}
