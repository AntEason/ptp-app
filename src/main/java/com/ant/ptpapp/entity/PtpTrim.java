package com.ant.ptpapp.entity;

import lombok.Data;

import java.util.Date;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-29 3:26 下午
 */
@Data
public class PtpTrim {
    /**
     * 上报地址
     */
    private String addrese;
    /**
     * 上报时间
     */
    private Date time;
}
