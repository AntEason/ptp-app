package com.ant.ptpapp.controller;


import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.*;
import com.ant.ptpapp.mapper.PtpReportMapper;
import com.ant.ptpapp.service.PtpReportService;
import com.ant.ptpapp.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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
@Slf4j
public class PtpReportController {
    @Autowired
    PtpReportService ptpReportService;

    @Autowired
    RedisUtil redisUtil;

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
        return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_USER);
    }


    @PostMapping("/wx/edit")
    @ApiOperation(value = "微信疫情上报",tags={"上报接口"})
    public GenericResponse edit(HttpServletRequest request ,@RequestBody ReqPtpReportEdit reqPtpReportEdit){
        PtpUserInfo ptpUserInfo= getUserInfo(request);
        try {
            return ptpReportService.editReport(reqPtpReportEdit,ptpUserInfo);
        } catch (ParseException e) {
            log.error(""+e);
            return GenericResponse.response(ServiceError.GLOBAL_ERR_REPORT_DATE);
        }
    }

    @PostMapping("/wx/sel")
    @ApiOperation(value = "获取当前行程上报记录",tags={"上报接口"})
    public GenericResponse sel(HttpServletRequest request,@RequestBody ReqWxSel reqWxSel){
        PtpUserInfo ptpUserInfo= getUserInfo(request);
        return ptpReportService.findByTripCode(ptpUserInfo,reqWxSel.getTripCode());
    }

    @PostMapping("/wx/repair")
    @ApiOperation(value = "微信疫情补报",tags={"上报接口"})
    public GenericResponse repair(HttpServletRequest request ,@RequestBody ReqPtpReportRepair reqPtpReportRepair){
        PtpUserInfo ptpUserInfo= getUserInfo(request);
        try {
            return ptpReportService.reportRepair(reqPtpReportRepair,ptpUserInfo);
        } catch (ParseException e) {
            log.error(""+e);
            return GenericResponse.response(ServiceError.GLOBAL_ERR_REPORT_DATE);
        }
    }

    @PostMapping("/wx/byPage")
    @ApiOperation(value = "微信我的申报记录",tags={"上报接口"})
    public GenericResponse byPage(HttpServletRequest request ,@RequestBody ReqPtpReport reqPtpReport){
        PtpUserInfo ptpUserInfo= getUserInfo(request);
        return ptpReportService.byPage(ptpUserInfo,reqPtpReport);
    }

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    public PtpUserInfo getUserInfo(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        String json = (String) redisUtil.get(token);
        if(StringUtils.isNoneEmpty(json)){
            return JSON.parseObject(json,PtpUserInfo.class);
        }
        return null;
    }


}

