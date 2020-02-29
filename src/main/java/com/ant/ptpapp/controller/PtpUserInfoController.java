package com.ant.ptpapp.controller;


import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqPtpUserInfoSel;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.service.WeChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */


@Api(value="用户信息",tags={"用户操作接口"})
@RestController
@Slf4j
@RequestMapping("/userInfo")
public class PtpUserInfoController {

    @Autowired
    PtpUserInfoService ptpUserInfoService;
    @PostMapping("/admin/sel")
    @ApiOperation(value = "用户管理查询",tags={"用户操作接口"})
    public GenericResponse userInfoSel( @RequestBody ReqPtpUserInfoSel reqPtpUserInfoSel)throws Exception{
        QueryWrapper<PtpUserInfo> queryWrapper=new QueryWrapper();
        if(StringUtils.isNoneEmpty(reqPtpUserInfoSel.getCondition())){
            queryWrapper.like("user_name",reqPtpUserInfoSel.getCondition()).or().like("user_phone",reqPtpUserInfoSel.getCondition());
        }
        queryWrapper.eq("user_type","1");
        if(StringUtils.isNoneEmpty(reqPtpUserInfoSel.getStartTime())){
            queryWrapper.apply("create_time >= date_format({0},'%Y-%m-%d %H:%i:%s')",reqPtpUserInfoSel.getStartTime());
        }
        if(StringUtils.isNoneEmpty(reqPtpUserInfoSel.getEndTime())) {
            queryWrapper.apply("create_time <= date_format({0},'%Y-%m-%d %H:%i:%s')",reqPtpUserInfoSel.getEndTime());
        }
        Page<PtpUserInfo> ptpDevicePage=new Page<>(reqPtpUserInfoSel.getPage(),reqPtpUserInfoSel.getPageSize());
        IPage<PtpUserInfo> result= ptpUserInfoService.page(ptpDevicePage,queryWrapper);
        if(result!=null){
            return GenericResponse.response(ServiceError.NORMAL,result);
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }

}

