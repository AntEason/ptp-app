package com.ant.ptpapp.service;

import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.PtpTrim;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqByDeviceIdSel;
import com.ant.ptpapp.entity.req.ReqPtpReport;
import com.ant.ptpapp.entity.req.ReqPtpReportEdit;
import com.ant.ptpapp.entity.req.ReqPtpReportRepair;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
public interface PtpReportService extends IService<PtpReport> {
    IPage<PtpReport> selectPageVo( ReqPtpReport reqPtpReport);

    IPage<PtpByDeviceInfo> findByDeviceInfo(ReqByDeviceIdSel reqByDeviceIdSel);

    GenericResponse editReport(ReqPtpReportEdit reqPtpReportEdit, PtpUserInfo ptpUserInfo) throws ParseException;

    GenericResponse reportRepair(ReqPtpReportRepair reqPtpReportEdit, PtpUserInfo ptpUserInfo) throws ParseException;

    GenericResponse findByTripCode(PtpUserInfo ptpUserInfo,String tripCode);


    GenericResponse byPage(PtpUserInfo ptpUserInfo,ReqPtpReport reqPtpReport);
}
