package com.ant.ptpapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.*;
import com.ant.ptpapp.entity.req.ReqByDeviceIdSel;
import com.ant.ptpapp.entity.req.ReqPtpReport;
import com.ant.ptpapp.entity.req.ReqPtpReportEdit;
import com.ant.ptpapp.entity.req.ReqPtpReportRepair;
import com.ant.ptpapp.mapper.PtpDeviceMapper;
import com.ant.ptpapp.mapper.PtpInReportMapper;
import com.ant.ptpapp.mapper.PtpReportMapper;
import com.ant.ptpapp.service.PtpReportService;
import com.ant.ptpapp.util.CollectionUtil;
import com.ant.ptpapp.util.IdWorker;
import com.ant.ptpapp.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
@Service
@Slf4j
public class PtpReportServiceImpl extends ServiceImpl<PtpReportMapper, PtpReport> implements PtpReportService {
    @Autowired
    PtpReportMapper ptpReportMapper;

    @Autowired
    PtpDeviceMapper ptpDeviceMapper;

    @Autowired
    PtpInReportMapper ptpInReportMapper;

    @Autowired
    RedisUtil redisUtil;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    @Override
    public IPage<PtpReport> selectPageVo( ReqPtpReport ptpReport) {
        Page<PtpReport> ptpReportPage=new Page<>(ptpReport.getPage(),ptpReport.getPageSize());
        return  ptpReportMapper.selectPageVo(ptpReportPage,ptpReport.getUserPhone(),ptpReport.getUserId(),ptpReport.getStartTime(),ptpReport.getEndTime(),ptpReport.getCondition());
    }

    @Override
    public IPage<PtpByDeviceInfo> findByDeviceInfo(ReqByDeviceIdSel reqByDeviceIdSel) {
        Page<PtpByDeviceInfo> ptpByDeviceInfoPage=new Page<>(reqByDeviceIdSel.getPage(),reqByDeviceIdSel.getPageSize());
        return ptpReportMapper.findByDeviceInfo(ptpByDeviceInfoPage,reqByDeviceIdSel.getDeviceId(),reqByDeviceIdSel.getStartTime(),reqByDeviceIdSel.getEndTime(),reqByDeviceIdSel.getCondition());
    }

    @Override
    public GenericResponse editReport(ReqPtpReportEdit reqPtpReportEdit, PtpUserInfo ptpUserInfo) throws ParseException {
        IdWorker worker = new IdWorker(1,1,1);
        Long user=ptpUserInfo.getUserInfoId();
        QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper<PtpDevice>();
        queryWrapper.eq("device_bt_name",reqPtpReportEdit.getBtName());
        PtpDevice ptpDevice =ptpDeviceMapper.selectOne(queryWrapper);
        if(null==ptpDevice){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_DEVICE);
        }
        if(ptpDevice.getDeviceType().equals(1)) {
            String tripCode = (String) redisUtil.get(String.valueOf(user));
            if(StringUtils.isNoneEmpty(tripCode)){
                return GenericResponse.response(ServiceError.GLOBAL_ERR_REPORTS);
            }
            //进闸上报
             tripCode = String.valueOf(worker.nextId());
            //行程编号缓存3小时
            redisUtil.set(String.valueOf(user), tripCode, 3 * 60 * 60 * 1000);
            PtpReport ptpReport = new PtpReport();
            ptpReport.setUserPhone(ptpUserInfo.getUserPhone());
            ptpReport.setUserName(ptpUserInfo.getUserName());
            ptpReport.setTripCode(tripCode);
            ptpReport.setUserId(ptpUserInfo.getUserInfoId());
            ptpReport.setGetDeviceId(ptpDevice.getEditDeviceId());
            ptpReport.setState(2);
            ptpReport.setGetReportTime(new Date());
            log.info("入闸上报 ======>"+ JSON.toJSONString(ptpReport));
            if(ptpReportMapper.insert(ptpReport)!=0) {
                Map<String,String> result=new HashMap<>();
                result.put("tripCode",tripCode);
                return GenericResponse.response(ServiceError.NORMAL,result);
            }
            //车厢上报
        }else if(ptpDevice.getDeviceType().equals(2)){
            String tripCode = (String) redisUtil.get(String.valueOf(user));
            if(StringUtils.isEmpty(tripCode)){
                return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_TRIP_CODE);
            }
            QueryWrapper<PtpInReport> ptp=new QueryWrapper<>();
            ptp.eq("trip_code",tripCode);
            ptp.eq("device_id",ptpDevice.getEditDeviceId());
            Integer count= ptpInReportMapper.selectCount(ptp);
            if(count>0){
                return GenericResponse.response(ServiceError.GLOBAL_ERR_PTP_IN_REPORT_EXISTED);
            }
            PtpInReport ptpInReport=new PtpInReport();
            ptpInReport.setCreateTime(new Date());
            ptpInReport.setDeviceId(ptpDevice.getEditDeviceId());
            ptpInReport.setTripCode(tripCode);
            log.info("车厢上报 ======>"+ JSON.toJSONString(ptpInReport));
            if(ptpInReportMapper.insert(ptpInReport)!=0){
                Map<String,String> result=new HashMap<>();
                result.put("tripCode",tripCode);
                return GenericResponse.response(ServiceError.NORMAL,result);
            }
            //出闸上报
        }else{
            String tripCode = (String) redisUtil.get(String.valueOf(user));
            if(StringUtils.isEmpty(tripCode)){
                return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_TRIP_CODE);
            }
            //进闸上报查询
            QueryWrapper<PtpReport> query=new QueryWrapper<>();
            query.eq("trip_code",tripCode);
            PtpReport report= ptpReportMapper.selectOne(query);
            if(report==null){
                redisUtil.delete(String.valueOf(user));
                return GenericResponse.response(ServiceError.GLOBAL_ERR_TRIP_CODE);
            }
            log.info("出闸（入闸上报） ======>"+ JSON.toJSONString(report));
            //进闸上报设备查询
            QueryWrapper<PtpDevice> ptpDeviceQueryWrapper=new QueryWrapper<>();
            ptpDeviceQueryWrapper.eq("edit_device_id",report.getGetDeviceId());
            PtpDevice getPtpDevice =ptpDeviceMapper.selectOne(ptpDeviceQueryWrapper);
            log.info("出闸（设备查询） ======>"+ JSON.toJSONString(report));
            //车厢上报查询
            QueryWrapper<PtpInReport> queryPtpInReport=new QueryWrapper<PtpInReport>();
            queryPtpInReport.eq("trip_code",tripCode);
            Integer count= ptpInReportMapper.selectCount(queryPtpInReport);
//            if(!getPtpDevice.getDeviceLine().equals(ptpDevice.getDeviceLine())&&count>1){
//                return GenericResponse.response(ServiceError.GLOBAL_ERR_IN_REPORT);
//            }
            report.setOutDeviceId(ptpDevice.getEditDeviceId());
            report.setOutReportTime(new Date());
            report.setState(1);
            report.setRemark("改行程已完成上报");
            if(ptpReportMapper.updateById(report)>0){
                Map<String,String> result=new HashMap<>();
                result.put("tripCode",tripCode);
                redisUtil.delete(String.valueOf(user));
                return GenericResponse.response(ServiceError.NORMAL,result);
            }
        }
        return  GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

    @Override
    public GenericResponse reportRepair(ReqPtpReportRepair reqPtpReportEdit, PtpUserInfo ptpUserInfo) throws ParseException {
        Long user=ptpUserInfo.getUserInfoId();
        String tripCode = (String) redisUtil.get(String.valueOf(user));
        PtpReport ptpReport = new PtpReport();
        ptpReport.setUserPhone(ptpUserInfo.getUserPhone());
        ptpReport.setUserName(ptpUserInfo.getUserName());
        ptpReport.setTripCode(tripCode);
        ptpReport.setUserId(ptpUserInfo.getUserInfoId());
        ptpReport.setRepairGet(reqPtpReportEdit.getRepairGet());
        ptpReport.setRepairLine(reqPtpReportEdit.getRepairLine());
        ptpReport.setRepairOut(reqPtpReportEdit.getRepairOut());
        ptpReport.setReportType(1);
        ptpReport.setState(1);
        ptpReport.setOutReportTime(new Date());
        UpdateWrapper<PtpReport> wrapper=new UpdateWrapper<>();
        wrapper.eq("trip_code",tripCode);
        if(ptpReportMapper.update(ptpReport,wrapper)!=0){
            Map<String,String> result=new HashMap<>();
            result.put("tripCode",tripCode);
            redisUtil.delete(String.valueOf(user));
            return GenericResponse.response(ServiceError.NORMAL,result);
        }else {
            return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
        }
    }

    @Override
    public GenericResponse findByTripCode(PtpUserInfo ptpUserInfo,String tripCode) {
        Long user=ptpUserInfo.getUserInfoId();
        if(StringUtils.isEmpty(tripCode)){
            tripCode = (String) redisUtil.get(String.valueOf(user));
        }
        log.info("tripCode ===========>"+tripCode);
        List<PtpReport> report= ptpReportMapper.findByTripCode(String.valueOf(user),tripCode);
        if(report!=null){
            if(report.size()>0){
                PtpReport ptpReport=report.get(0);
                List<PtpTrim> ptpTrims=  ptpReportMapper.findByTripCodeInReport(ptpReport.getTripCode());
                ptpReport.setInReports(ptpTrims);
                return GenericResponse.response(ServiceError.NORMAL,ptpReport);
            }
            return GenericResponse.response(ServiceError.NORMAL);
        }else{
            return GenericResponse.response(ServiceError.NORMAL);
        }

    }

    @Override
    public GenericResponse byPage(PtpUserInfo ptpUserInfo, ReqPtpReport ptpReport) {
        Long user=ptpUserInfo.getUserInfoId();
        Page<PtpReport> ptpReportPage=new Page<>(ptpReport.getPage(),ptpReport.getPageSize());
        IPage<PtpReport> ptpReportIPage=  ptpReportMapper.findByTripCodePage(ptpReportPage,String.valueOf(user));
        if(ptpReportIPage!=null){
            List<PtpReport> reports=ptpReportIPage.getRecords();
            if(reports.size()>0){
                for (PtpReport report:reports){
                    List<PtpTrim> ptpTrims=  ptpReportMapper.findByTripCodeInReport(report.getTripCode());
                    report.setInReports(ptpTrims);
                }
                ptpReportIPage.setRecords(reports);
                return GenericResponse.response(ServiceError.NORMAL,ptpReportIPage);
            }
            return GenericResponse.response(ServiceError.NORMAL,ptpReportIPage);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);

    }
}
