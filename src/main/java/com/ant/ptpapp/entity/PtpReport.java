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
    private String getDeviceId;

    /**
     * 出闸设备编号
     */
    private String outDeviceId;

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

    /**
     * 上报类型（0.正常上报 1.补报
     */
    private Integer reportType;
    /**
     * 补报进闸口
     */
    private String repairGet;

    /**
     * 补报线路
     */
    private String repairLine;

    /**
     * 补报出闸口
     */
    private String repairOut;
    /**
     * 用户名称
     */
    private  String userName;
    /**
     * 补报进闸地址
     */
    @TableField(exist = false)
    private PtpTrim getReport;
    /**
     * 出站进闸地址
     */
    @TableField(exist = false)
    private PtpTrim outReport;
    /**
     * 车厢上报地址
     */
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

    public String getGetDeviceId() {
        return getDeviceId;
    }

    public void setGetDeviceId(String getDeviceId) {
        this.getDeviceId = getDeviceId;
    }

    public String getOutDeviceId() {
        return outDeviceId;
    }

    public void setOutDeviceId(String outDeviceId) {
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

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getRepairGet() {
        return repairGet;
    }

    public void setRepairGet(String repairGet) {
        this.repairGet = repairGet;
    }

    public String getRepairLine() {
        return repairLine;
    }

    public void setRepairLine(String repairLine) {
        this.repairLine = repairLine;
    }

    public String getRepairOut() {
        return repairOut;
    }

    public void setRepairOut(String repairOut) {
        this.repairOut = repairOut;
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
                ", userName=" + userName +
                ", userId=" + userId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", reportType=" + reportType +
                ", repairGet=" + repairGet +
                ", repairLine=" + repairLine +
                ", repairOut=" + repairOut +
                ", remark=" + remark +
                ", createTime=" + createTime +
                "}";
    }
}
