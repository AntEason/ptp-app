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
     * 创建时间
     */
    private LocalDateTime createTime;


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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PtpUserInfo{" +
        "userInfoId=" + userInfoId +
        ", userPhone=" + userPhone +
        ", userPwd=" + userPwd +
        ", createTime=" + createTime +
        "}";
    }
}
