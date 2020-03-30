package com.ant.ptpapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpDevice;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqGetPhoneNum;
import com.ant.ptpapp.entity.req.ReqUpdatePhoneNum;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.ant.ptpapp.service.PtpDeviceService;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.service.WeChatService;
import com.ant.ptpapp.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-02-28 5:41 下午
 */
@Api(value="登陆接口",tags={"登陆接口"})
@RestController
@Slf4j
@RequestMapping("/lind-auth")
public class LoginController {

        @Autowired
        WeChatService weChatService;

        @Autowired
        PtpUserInfoService ptpUserInfoService;

        @Autowired
        private PtpDeviceService ptpDeviceService;

        @Autowired
        RedisUtil redisUtil;
        @PostMapping("/wx/login")
        @ApiOperation(value = "小程序用户登录",tags={"登陆接口"})
        public GenericResponse wxLogin(@RequestBody ReqUserInfo reqUserInfo)throws Exception{
            //通过微信编号登陆
            if(StringUtils.isNoneEmpty(reqUserInfo.getWxCode())) {
                JSONObject jsonObject = JSONObject.parseObject(weChatService.jcode2Session(reqUserInfo.getWxCode()));
                String openid = jsonObject.getString("openid");
                String sessionKey = jsonObject.getString("session_key");
                QueryWrapper<PtpUserInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("wx_open_id", openid);
                queryWrapper.eq("user_type", "1");
                PtpUserInfo ptpUserInfo = ptpUserInfoService.getOne(queryWrapper);
                if (ptpUserInfo == null&&StringUtils.isNotEmpty(openid)) {
                    ptpUserInfo = new PtpUserInfo();
                    ptpUserInfo.setUserName(RandomName.randomName(true, 3));
                    ptpUserInfo.setWxOpenId(openid);
                    ptpUserInfo.setUserPwd(MD5Util.getMD5("123456"));
                    ptpUserInfo.setUserType(1);
                    ptpUserInfoService.save(ptpUserInfo);
                }
                ptpUserInfo.setSessionKey(sessionKey);
                String token = JwtTokenUtil.generateToken(ptpUserInfo);
                log.info("=====>" + JSON.toJSONString(ptpUserInfo));
                redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
                Map<String, Object> result = new HashMap<>();
                result.put("token", token);
                result.put("userInfo", ptpUserInfo);
                return GenericResponse.response(ServiceError.NORMAL, result);
            }
            return GenericResponse.response(ServiceError.UN_KNOW_ERROR);
        }

        @PostMapping("/admin/login")
        @ApiOperation(value = "后台用户登录",tags={"登陆接口"})
        public GenericResponse adminLogin( @RequestBody ReqUserInfo reqUserInfo)throws Exception{
            return ptpUserInfoService.adminLogin(reqUserInfo);
        }


        @PostMapping("/admin/logout")
        @ApiOperation(value = "后台用户推出登录",tags={"登陆接口"})
        public GenericResponse adminLogin(HttpServletRequest request)throws Exception{
            String authHeader = request.getHeader("Authorization").substring("Bearer ".length());
            redisUtil.delete(authHeader);
            return GenericResponse.response(ServiceError.NORMAL);
        }

        @PostMapping("/wx/bindPhone")
        @ApiOperation(value = "微信绑定电话",tags={"登陆接口"})
        public GenericResponse bindPhone(HttpServletRequest request, @RequestBody ReqGetPhoneNum reqGetPhoneNum)throws Exception{
            PtpUserInfo ptpUserInfo=  getUserInfo(request);
            if(ptpUserInfo==null){
                return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN);
            }
            String userPhone= WechatDecryptDataUtil.decryptData(reqGetPhoneNum.getEncryptedData(),ptpUserInfo.getSessionKey(),reqGetPhoneNum.getIv());
            userPhone=JSONObject.parseObject(userPhone).getString("phoneNumber");
            QueryWrapper<PtpUserInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("wx_open_id",ptpUserInfo.getWxOpenId());
            PtpUserInfo ptpUserInfos =ptpUserInfoService.getOne(queryWrapper);
            ptpUserInfo.setUserPhone(userPhone);
            ptpUserInfo.setUserInfoId(ptpUserInfos.getUserInfoId());
            boolean result=ptpUserInfoService.updateById(ptpUserInfo);
            if(result) {
                String token = request.getHeader("Authorization").substring("Bearer ".length());
                redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
                log.info("=======>"+JSON.toJSONString(ptpUserInfo));
                return GenericResponse.response(ServiceError.NORMAL);
            }else{
                return GenericResponse.response(ServiceError.GLOBAL_ERR_BIND_PHONE);
            }
        }
        @PostMapping("/wx/binUserName")
        @ApiOperation(value = "微信绑定用户名称",tags={"登陆接口"})
        public GenericResponse binUserName(HttpServletRequest request, @RequestBody ReqGetPhoneNum reqGetPhoneNum)throws Exception{
            PtpUserInfo ptpUserInfo=  getUserInfo(request);
            QueryWrapper<PtpUserInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("wx_open_id",ptpUserInfo.getWxOpenId());
            ptpUserInfo =ptpUserInfoService.getOne(queryWrapper);
            ptpUserInfo.setUserName(reqGetPhoneNum.getUserName());
            boolean result=ptpUserInfoService.updateById(ptpUserInfo);
            if(result) {
                String token = request.getHeader("Authorization").substring("Bearer ".length());
                redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
                return GenericResponse.response(ServiceError.NORMAL);
            }else{
                return GenericResponse.response(ServiceError.GLOBAL_ERR_BIND_PHONE);
            }
        }
    @GetMapping("/getAllQRCode")
    @ApiOperation(value = "获取所有设备的小程序码",tags={"登陆接口"})
    public GenericResponse getAllQRCode(){
        boolean result=true;
        QueryWrapper queryWrapper =new QueryWrapper<>();
        queryWrapper.isNull("recode_path");
        List<PtpDevice> list= ptpDeviceService.list(queryWrapper);
        for (PtpDevice ptpDevice: list) {
            String imageName=ptpDevice.getDeviceBtName()+".png";
            if(weChatService.createQRCode("deviceName="+ptpDevice.getDeviceBtName(),imageName)){
                ptpDevice.setRecodePath("QRCode/" +imageName);
                ptpDeviceService.updateById(ptpDevice);
            }else{
                result=false;
                break;
            }
        }
        if(result){
            return GenericResponse.response(ServiceError.NORMAL,"创建小程序码成功");
        }else{
            return GenericResponse.response(ServiceError.GLOBAL_ERR_PTP_CREATE_QE_CODE);
        }
    }

    @PostMapping("/wx/updatePhone")
    @ApiOperation(value = "修改电话号码",tags={"登陆接口"})
    public GenericResponse updatePhone(HttpServletRequest request, @RequestBody ReqUpdatePhoneNum reqUpdatePhoneNum)throws Exception{
        PtpUserInfo ptpUserInfo=  getUserInfo(request);
        if(ptpUserInfo==null){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN);
        }
        QueryWrapper<PtpUserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("wx_open_id",ptpUserInfo.getWxOpenId());
        PtpUserInfo ptpUserInfos =ptpUserInfoService.getOne(queryWrapper);
        ptpUserInfo.setUserPhone(reqUpdatePhoneNum.getPhoneNum());
        ptpUserInfo.setUserInfoId(ptpUserInfos.getUserInfoId());
        boolean result=ptpUserInfoService.updateById(ptpUserInfo);
        if(result) {
            String token = request.getHeader("Authorization").substring("Bearer ".length());
            redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
            log.info("=======>"+JSON.toJSONString(ptpUserInfo));
            return GenericResponse.response(ServiceError.NORMAL);
        }else{
            return GenericResponse.response(ServiceError.GLOBAL_ERR_BIND_PHONE);
        }
    }


    @PostMapping("/wx/delPhone")
    @ApiOperation(value = "修改电话号码",tags={"登陆接口"})
    public GenericResponse delPhone(HttpServletRequest request)throws Exception{
        PtpUserInfo ptpUserInfo=  getUserInfo(request);
        if(ptpUserInfo==null){
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN);
        }
        QueryWrapper<PtpUserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("wx_open_id",ptpUserInfo.getWxOpenId());
        PtpUserInfo ptpUserInfos =ptpUserInfoService.getOne(queryWrapper);
        ptpUserInfo.setUserPhone("");
        ptpUserInfo.setUserInfoId(ptpUserInfos.getUserInfoId());
        boolean result=ptpUserInfoService.updateById(ptpUserInfo);
        if(result) {
            String token = request.getHeader("Authorization").substring("Bearer ".length());
            redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
            log.info("=======>"+JSON.toJSONString(ptpUserInfo));
            return GenericResponse.response(ServiceError.NORMAL);
        }else{
            return GenericResponse.response(ServiceError.GLOBAL_ERR_BIND_PHONE);
        }
    }
    /**
     * 获取用户信息
     * @param request
     * @return
     */
    public  PtpUserInfo getUserInfo(HttpServletRequest request){
            String token = request.getHeader("Authorization");
            if(StringUtils.isEmpty(token)){return null;}
            token=token.substring("Bearer ".length());
            String json = (String) redisUtil.get(token);
            if(StringUtils.isNoneEmpty(json)){
                return JSON.parseObject(json,PtpUserInfo.class);
            }
            return null;
        }
    }
