package com.ant.ptpapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
public class PtpInReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车厢上报编号
     */
    @TableId(value = "ptp_in_report_id", type = IdType.AUTO)
    private Long ptpInReportId;

    /**
     * 设备编号
     */
    private Long deviceId;

    /**
     * 行程编号
     */
    private String tripCode;

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getPtpInReportId() {
        return ptpInReportId;
    }

    public void setPtpInReportId(Long ptpInReportId) {
        this.ptpInReportId = ptpInReportId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PtpInReport{" +
        "ptpInReportId=" + ptpInReportId +
        ", deviceId=" + deviceId +
        ", tripCode=" + tripCode +
        ", createTime=" + createTime +
        "}";
    }
}
