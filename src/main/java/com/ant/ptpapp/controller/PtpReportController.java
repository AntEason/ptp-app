package com.ant.ptpapp.controller;


import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.req.ReqByDeviceIdSel;
import com.ant.ptpapp.entity.req.ReqGetPhoneNum;
import com.ant.ptpapp.entity.req.ReqPtpReport;
import com.ant.ptpapp.mapper.PtpReportMapper;
import com.ant.ptpapp.service.PtpReportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
@RestController
@Api(value="上报接口",tags={"上报接口"})
@RequestMapping("/ptpReport")
public class PtpReportController {
    @Autowired
    PtpReportService ptpReportService;
    @PostMapping("/admin/sel")
    @ApiOperation(value = "后端查询轨迹",tags={"上报接口"})
    public GenericResponse ptpReportSel( @RequestBody ReqPtpReport reqPtpReport){
        IPage<PtpReport> result= ptpReportService.selectPageVo(reqPtpReport);
        if(result!=null){
            return GenericResponse.response(ServiceError.NORMAL,result);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }
    @PostMapping("/admin/byDeviceId")
    @ApiOperation(value = "设备详情",tags={"上报接口"})
    public GenericResponse byDeviceId(@RequestBody ReqByDeviceIdSel byDeviceIdSel){
        Page<PtpByDeviceInfo> ptpByDeviceInfoIPage=new Page<PtpByDeviceInfo>();
        List<PtpByDeviceInfo> list=new ArrayList<>();
        if("2".equals(byDeviceIdSel.getDeviceType())){
            IPage<PtpByDeviceInfo> result=  ptpReportService.findByDeviceInfo(byDeviceIdSel);
            if(result!=null){
                return GenericResponse.response(ServiceError.NORMAL,result);
            }
        }else{
            QueryWrapper<PtpReport> queryWrapper=new QueryWrapper<>();
            if("1".equals(byDeviceIdSel.getDeviceType())){
                queryWrapper.eq("get_device_id",byDeviceIdSel.getDeviceId());
                if(StringUtils.isNoneEmpty(byDeviceIdSel.getStartTime())){
                    queryWrapper.apply("get_report_time >= date_format({0},'%Y-%m-%d %H:%i:%s')",byDeviceIdSel.getStartTime());
                }
                if(StringUtils.isNoneEmpty(byDeviceIdSel.getEndTime())) {
                    queryWrapper.apply("get_report_time <= date_format({0},'%Y-%m-%d %H:%i:%s')",byDeviceIdSel.getEndTime());
                }
            }else{
                queryWrapper.eq("out_device_id",byDeviceIdSel.getDeviceId());
                if(StringUtils.isNoneEmpty(byDeviceIdSel.getStartTime())){
                    queryWrapper.apply("out_report_time >= date_format({0},'%Y-%m-%d %H:%i:%s')",byDeviceIdSel.getStartTime());
                }
                if(StringUtils.isNoneEmpty(byDeviceIdSel.getEndTime())) {
                    queryWrapper.apply("out_report_time <= date_format({0},'%Y-%m-%d %H:%i:%s')",byDeviceIdSel.getEndTime());
                }
            }
            if(StringUtils.isNoneEmpty(byDeviceIdSel.getCondition())){
                queryWrapper.like("user_name",byDeviceIdSel.getCondition()).or().like("user_phone",byDeviceIdSel.getCondition());
            }
            Page<PtpReport> ptpDevicePage=new Page<>(byDeviceIdSel.getPage(),byDeviceIdSel.getPageSize());
            IPage<PtpReport> reportIPage= ptpReportService.page(ptpDevicePage,queryWrapper);
            if(reportIPage!=null){
                for (PtpReport ptpReport:reportIPage.getRecords()) {
                    PtpByDeviceInfo ptpByDeviceInfo=new PtpByDeviceInfo();
                    if("1".equals(byDeviceIdSel.getDeviceType())){
                        ptpByDeviceInfo.setTime(ptpReport.getGetReportTime());
                    }else{
                        ptpByDeviceInfo.setTime(ptpReport.getOutReportTime());
                    }
                    ptpByDeviceInfo.setUserId(ptpReport.getUserId().toString());
                    ptpByDeviceInfo.setUserName(ptpReport.getUserName());
                    ptpByDeviceInfo.setUserPhone(ptpReport.getUserPhone());
                    list.add(ptpByDeviceInfo);
                }
                ptpByDeviceInfoIPage.setRecords(list);
                ptpByDeviceInfoIPage.setTotal(reportIPage.getTotal());
                ptpByDeviceInfoIPage.setSize(reportIPage.getSize());
                ptpByDeviceInfoIPage.setPages(reportIPage.getPages());
                return GenericResponse.response(ServiceError.NORMAL,ptpByDeviceInfoIPage);
            }
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

}

