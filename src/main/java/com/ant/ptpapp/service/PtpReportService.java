package com.ant.ptpapp.service;

import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.PtpTrim;
import com.ant.ptpapp.entity.req.ReqByDeviceIdSel;
import com.ant.ptpapp.entity.req.ReqPtpReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
