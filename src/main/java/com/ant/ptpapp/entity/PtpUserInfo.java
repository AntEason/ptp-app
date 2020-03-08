package com.ant.ptpapp.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yichen
 * @since 2020-02-28
 */
public class PtpUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息主键编号
     */
    @TableId(value = "user_info_id", type = IdType.AUTO)
    private Long userInfoId;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 微信OpenId
     */
    private String wxOpenId;

    /**
     * 用户类型（1.小程序 2.后台登陆）
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer userType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户名称
     */
    private String userName;

    @TableField(exist = false)
    private String sessionKey;

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "PtpUserInfo{" +
                "userInfoId=" + userInfoId +
                ", userPhone=" + userPhone +
                ", userPwd=" + userPwd +
                ", wxOpenId=" + wxOpenId +
                ", userType=" + userType +
                ", createTime=" + createTime +
                "}";
    }
}
