package com.ant.ptpapp.service.impl;

import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.PtpTrim;
import com.ant.ptpapp.entity.req.ReqByDeviceIdSel;
import com.ant.ptpapp.entity.req.ReqPtpReport;
import com.ant.ptpapp.mapper.PtpReportMapper;
import com.ant.ptpapp.service.PtpReportService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
@Service
public class PtpReportServiceImpl extends ServiceImpl<PtpReportMapper, PtpReport> implements PtpReportService {
    @Autowired
    PtpReportMapper ptpReportMapper;

    @Override
    public IPage<PtpReport> selectPageVo( ReqPtpReport ptpReport) {
        Page<PtpReport> ptpReportPage=new Page<>(ptpReport.getPage(),ptpReport.getPageSize());
        return  ptpReportMapper.selectPageVo(ptpReportPage,ptpReport.getUserPhone(),ptpReport.getUserPhone(),ptpReport.getStartTime(),ptpReport.getEndTime(),ptpReport.getCondition());
    }

    @Override
    public IPage<PtpByDeviceInfo> findByDeviceInfo(ReqByDeviceIdSel reqByDeviceIdSel) {
        Page<PtpByDeviceInfo> ptpByDeviceInfoPage=new Page<>(reqByDeviceIdSel.getPage(),reqByDeviceIdSel.getPageSize());
        return ptpReportMapper.findByDeviceInfo(ptpByDeviceInfoPage,reqByDeviceIdSel.getDeviceId(),reqByDeviceIdSel.getStartTime(),reqByDeviceIdSel.getEndTime(),reqByDeviceIdSel.getCondition());
    }
}
