package com.ant.ptpapp.service;

import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */
public interface PtpUserInfoService extends IService<PtpUserInfo> {

    GenericResponse adminLogin(ReqUserInfo reqUserInfo);

}
