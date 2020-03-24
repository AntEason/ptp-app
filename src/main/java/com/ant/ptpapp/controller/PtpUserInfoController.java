package com.ant.ptpapp.controller;


import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqPtpUserInfoSel;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.service.WeChatService;
import com.ant.ptpapp.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    RedisUtil redisUtil;

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


    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @PostMapping("/wx/getUserInfo")
    @ApiOperation(value = "获取当前用户信息",tags={"用户操作接口"})
    public GenericResponse getUserInfo(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN);
        }
        token=token.substring("Bearer ".length());
        String json = (String) redisUtil.get(token);
        if(StringUtils.isNoneEmpty(json)){
            return GenericResponse.response(ServiceError.NORMAL,JSON.parseObject(json,PtpUserInfo.class));
        }
        return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
    }
}

