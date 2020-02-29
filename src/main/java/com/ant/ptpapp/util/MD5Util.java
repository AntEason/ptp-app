package com.ant.ptpapp.util;

import org.springframework.util.DigestUtils;

import java.util.Arrays;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-28 9:58 上午
 */
public class MD5Util {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";
    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public static void main(String[] args) {
        System.out.println("args = " + getMD5("123456"));
    }
}
