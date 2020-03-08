package com.ant.ptpapp.controller;


import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.req.ReqBtNameSel;
import com.ant.ptpapp.entity.req.ReqPtpDevice;
import com.ant.ptpapp.entity.req.ReqPtpDeviceSel;
import com.ant.ptpapp.service.PtpDeviceService;
import com.ant.ptpapp.service.WeChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/ptpDevice")
@Api(value="设备管理",tags={"设备操作接口"})
public class PtpDeviceController {

    @Autowired
    PtpDeviceService ptpDeviceService;

    @Autowired
    WeChatService weChatService;

   private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

    @PostMapping("/admin/save")
    @ApiOperation(value = "设备添加",tags={"设备操作接口"})
    public GenericResponse ptpDeviceAdd(@RequestBody ReqPtpDevice reqPtpDevice)throws Exception{
        String json=JSON.toJSONString(reqPtpDevice);
        PtpDevice ptpDevice=JSON.parseObject(json, PtpDevice.class);
        QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper();
        queryWrapper.eq("edit_device_id",ptpDevice.getEditDeviceId());
        if(ptpDeviceService.count(queryWrapper)>0){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_DEVICE_CODE);
        }
        if(StringUtils.isNoneEmpty(ptpDevice.getDeviceBtName())){
            String imageName=ptpDevice.getDeviceBtName()+".png";
            if(weChatService.createQRCode("deviceName="+ptpDevice.getDeviceBtName(),imageName)){
                ptpDevice.setRecodePath("QRCode/" +imageName);
            }else{
                return GenericResponse.response(ServiceError.GLOBAL_ERR_PTP_CREATE_QE_CODE);
            }
        }
         boolean result=  ptpDeviceService.save(ptpDevice);
         if(result){
             return GenericResponse.response(ServiceError.NORMAL);
         }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

    @PostMapping("/admin/edit")
    @ApiOperation(value = "设备修改",tags={"设备操作接口"})
    public GenericResponse ptpDeviceEdit(@RequestBody ReqPtpDevice reqPtpDevice)throws Exception{
        //判断修改设备id相同
        QueryWrapper<PtpDevice> query=new QueryWrapper();
        query.eq("edit_device_id",reqPtpDevice.getEditDeviceId());
        if(ptpDeviceService.count(query)>0){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_DEVICE_CODE);
        }
        String json=JSON.toJSONString(reqPtpDevice);
        PtpDevice ptpDevice=JSON.parseObject(json, PtpDevice.class);
        UpdateWrapper<PtpDevice> updateWrapper=new UpdateWrapper();
        updateWrapper.eq("device_id",ptpDevice.getDeviceId());
        String btName=ptpDevice.getDeviceBtName();
        if(StringUtils.isNoneEmpty(btName)){
            //删除之前已存在的文件
            QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper();
            queryWrapper.eq("device_id",ptpDevice.getDeviceId());
            PtpDevice device= ptpDeviceService.getOne(queryWrapper);
            String path= System.getProperty("user.dir")+"/"+device.getRecodePath();
            File file=new File(path);
            if(file.isFile()) {
                file.delete();
            }
            //创建新的小程序码文件
            String imageName=btName+".png";
            if(weChatService.createQRCode("deviceName="+btName,imageName)){
                ptpDevice.setRecodePath("QRCode/" +imageName);
            }else{
                return GenericResponse.response(ServiceError.GLOBAL_ERR_PTP_CREATE_QE_CODE);
            }
        }
        boolean result=  ptpDeviceService.update(ptpDevice,updateWrapper);
        if(result){
            return GenericResponse.response(ServiceError.NORMAL);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

    @PostMapping("/admin/sel")
    @ApiOperation(value = "设备查询",tags={"设备操作接口"})
    public GenericResponse ptpDeviceSel(@RequestBody ReqPtpDeviceSel reqPtpDeviceSel)throws Exception{
        QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper();
        if(StringUtils.isNoneEmpty(reqPtpDeviceSel.getCondition())){
            queryWrapper.like("device_addrese",reqPtpDeviceSel.getCondition()).or().like("device_bt_name",reqPtpDeviceSel.getCondition());
        }
        if(StringUtils.isNoneEmpty(reqPtpDeviceSel.getStartTime())){
            queryWrapper.apply("installation_time >= date_format({0},'%Y-%m-%d %H:%i:%s')",reqPtpDeviceSel.getStartTime());
        }
        if(StringUtils.isNoneEmpty(reqPtpDeviceSel.getEndTime())) {
            queryWrapper.apply("installation_time <= date_format({0},'%Y-%m-%d %H:%i:%s')",reqPtpDeviceSel.getEndTime());
        }
        Page<PtpDevice> ptpDevicePage=new Page<>(reqPtpDeviceSel.getPage(),reqPtpDeviceSel.getPageSize());
        IPage<PtpDevice> result= ptpDeviceService.page(ptpDevicePage,queryWrapper);
        if(result!=null){
            return GenericResponse.response(ServiceError.NORMAL,result);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

    @PostMapping("/admin/del")
    @ApiOperation(value = "设备删除",tags={"设备操作接口"})
    public GenericResponse ptpDeviceDel(@RequestParam @ApiParam("系统设备编号") String deviceId)throws Exception{
        QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper();
        queryWrapper.eq("device_id",deviceId);
        PtpDevice device= ptpDeviceService.getOne(queryWrapper);
        String path= System.getProperty("user.dir")+"/"+device.getRecodePath();
        //删除之前已存在的文件
        File file=new File(path);
        if(file.isFile()) {
            file.delete();
        }
        boolean result= ptpDeviceService.remove(queryWrapper);
        if(result){
            return GenericResponse.response(ServiceError.NORMAL);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

    @PostMapping("/wx/byBtName")
    @ApiOperation(value = "根据蓝牙名称查询",tags={"设备操作接口"})
    public GenericResponse byBtName(@RequestBody ReqBtNameSel reqBtNameSel)throws Exception{
        QueryWrapper<PtpDevice> queryWrapper=new QueryWrapper();
        queryWrapper.eq("device_bt_name",reqBtNameSel.getBtName());
        PtpDevice result= ptpDeviceService.getOne(queryWrapper);
        if(result!=null){
            return GenericResponse.response(ServiceError.NORMAL,result);
        }else{
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_DEVICE);
        }
    }
}

