package com.ant.ptpapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */
public class PtpDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备表主键编号
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 蓝牙名称
     */
    private String deviceBtName;

    /**
     * 安装时间
     */
    private LocalDateTime installationTime;

    /**
     * 1.进闸口 2.车厢  3.出闸口
     */
    private Integer deviceType;

    /**
     * 设备安装地址
     */
    private String deviceAddrese;

    /**
     * 设备线路
     */
    private Integer deviceLine;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 设备编号
     */
    private Integer editDeviceId;


    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceBtName() {
        return deviceBtName;
    }

    public void setDeviceBtName(String deviceBtName) {
        this.deviceBtName = deviceBtName;
    }

    public LocalDateTime getInstallationTime() {
        return installationTime;
    }

    public void setInstallationTime(LocalDateTime installationTime) {
        this.installationTime = installationTime;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceAddrese() {
        return deviceAddrese;
    }

    public void setDeviceAddrese(String deviceAddrese) {
        this.deviceAddrese = deviceAddrese;
    }

    public Integer getDeviceLine() {
        return deviceLine;
    }

    public void setDeviceLine(Integer deviceLine) {
        this.deviceLine = deviceLine;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getEditDeviceId() {
        return editDeviceId;
    }

    public void setEditDeviceId(Integer editDeviceId) {
        this.editDeviceId = editDeviceId;
    }

    @Override
    public String toString() {
        return "PtpDevice{" +
        "deviceId=" + deviceId +
        ", deviceName=" + deviceName +
        ", deviceBtName=" + deviceBtName +
        ", installationTime=" + installationTime +
        ", deviceType=" + deviceType +
        ", deviceAddrese=" + deviceAddrese +
        ", deviceLine=" + deviceLine +
        ", createTime=" + createTime +
        ", editDeviceId=" + editDeviceId +
        "}";
    }
}
