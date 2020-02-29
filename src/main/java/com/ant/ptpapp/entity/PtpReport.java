package com.ant.ptpapp.entity;

import com.ant.ptpapp.entity.req.ReqPtpDevice;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
public class PtpReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上报编号
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    /**
     * 行程编号
     */
    private String tripCode;

    /**
     * 上报时间
     */
    private Date getReportTime;

    /**
     * 进闸设备编号
     */
    private Long getDeviceId;

    /**
     * 出闸设备编号
     */
    private Long outDeviceId;

    /**
     * 出闸上报时间
     */
    private Date outReportTime;

    /**
     * 补充资料
     */
    private String remark;

    /**
     * 上报用户编号
     */
    private Long userId;

    /**
     * 上报用户手机号
     */
    private String userPhone;

    /**
     * 1.通行 2.未通行
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;


    private  String userName;
    @TableField(exist = false)
    private PtpTrim getReport;
    @TableField(exist = false)
    private PtpTrim outReport;
    @TableField(exist = false)
    private List<PtpTrim> inReports;

    public PtpTrim getGetReport() {
        return getReport;
    }

    public void setGetReport(PtpTrim getReport) {
        this.getReport = getReport;
    }

    public PtpTrim getOutReport() {
        return outReport;
    }

    public void setOutReport(PtpTrim outReport) {
        this.outReport = outReport;
    }

    public List<PtpTrim> getInReports() {
        return inReports;
    }

    public void setInReports(List<PtpTrim> inReports) {
        this.inReports = inReports;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public Date getGetReportTime() {
        return getReportTime;
    }

    public void setGetReportTime(Date getReportTime) {
        this.getReportTime = getReportTime;
    }

    public Long getGetDeviceId() {
        return getDeviceId;
    }

    public void setGetDeviceId(Long getDeviceId) {
        this.getDeviceId = getDeviceId;
    }

    public Long getOutDeviceId() {
        return outDeviceId;
    }

    public void setOutDeviceId(Long outDeviceId) {
        this.outDeviceId = outDeviceId;
    }

    public Date getOutReportTime() {
        return outReportTime;
    }

    public void setOutReportTime(Date outReportTime) {
        this.outReportTime = outReportTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "PtpReport{" +
        "reportId=" + reportId +
        ", tripCode=" + tripCode +
        ", getReportTime=" + getReportTime +
        ", getDeviceId=" + getDeviceId +
        ", outDeviceId=" + outDeviceId +
        ", outReportTime=" + outReportTime +
        ", remark=" + remark +
        ", userId=" + userId +
        ", userPhone=" + userPhone +
        ", state=" + state +
        ", createTime=" + createTime +
        "}";
    }
}
