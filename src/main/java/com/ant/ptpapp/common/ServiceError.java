package com.ant.ptpapp.common;

public enum ServiceError {

    NORMAL(1, "操作成功"),
    UN_KNOW_ERROR(-1, "未知错误"),

    /** Global Error */
    GLOBAL_ERR_NO_SIGN_IN(-10001,"未登录或登录过期/Not sign in"),
    GLOBAL_ERR_NO_CODE(-10002,"code错误/error code"),
    GLOBAL_ERR_NO_AUTHORITY(-10003, "没有操作权限/No operating rights"),
    GLOBAL_ERR_NO_USER(-10004, "未查询到用户,或者帐号密码错误"),
    GLOBAL_ERR_BIND_PHONE(-10005,"绑定手机号失败"),
    GLOBAL_ERR_REPORT(-10006,"上报疫情势失败"),
    GLOBAL_ERR_NO_TRIP_CODE(-10007,"上报错误，进闸行为未上报"),
    GLOBAL_ERR_IN_REPORT(-10008,"车厢上报数量不够"),
    GLOBAL_ERR_REPORT_DATE(-10009,"上报时间格式错误"),
    GLOBAL_ERR_DEVICE_CODE(-10010,"改设备已存在"),
    GLOBAL_ERR_NO_DEVICE(-10011,"改设备不存在"),
    GLOBAL_ERR_REPORTS(-10012,"当前上报已存在"),
    GLOBAL_ERR_GET_REPORTS(-10013,"未查到最近上报记录"),
    GLOBAL_ERR_TRIP_CODE(-10014,"行程上报错误，请填写补报申请"),
    GLOBAL_ERR_PTP_IN_REPORT_EXISTED(-10015,"改设备已经申报"),
    GLOBAL_ERR_PTP_CREATE_QE_CODE(-10016,"创建所有的小程序码失败"),
    ;


    private int code;
    private String msg;

    private ServiceError(int code, String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
