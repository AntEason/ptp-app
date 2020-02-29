package com.ant.ptpapp.entity;

import lombok.Data;

import java.util.Date;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 5:10 下午
 */
@Data
public class PtpByDeviceInfo {
    Date time;
    String userName;
    String userId;
    String userPhone;
}
