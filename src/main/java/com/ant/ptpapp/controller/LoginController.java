package com.ant.ptpapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqGetPhoneNum;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.service.WeChatService;
import com.ant.ptpapp.util.JwtTokenUtil;
import com.ant.ptpapp.util.MD5Util;
import com.ant.ptpapp.util.RedisUtil;
import com.ant.ptpapp.util.wxUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        RedisUtil redisUtill;
        @PostMapping("/wx/login")
        @ApiOperation(value = "小程序用户登录",tags={"登陆接口"})
        public GenericResponse wxLogin(@RequestBody ReqUserInfo reqUserInfo)throws Exception{
            //通过微信编号登陆
            if(StringUtils.isNoneEmpty(reqUserInfo.getWxCode())){
               JSONObject jsonObject=JSONObject.parseObject(weChatService.jcode2Session(reqUserInfo.getWxCode())) ;
               String openid= jsonObject.getString("openid");
               String sessionKey= jsonObject.getString("session_key");
               QueryWrapper<PtpUserInfo>  queryWrapper=new QueryWrapper<>();
               queryWrapper.eq("wx_open_id",openid);
               queryWrapper.eq("user_type","1");
               PtpUserInfo ptpUserInfo= ptpUserInfoService.getOne(queryWrapper);
               if(ptpUserInfo==null){
                  String userPhone= wxUtil.decrypt(sessionKey,reqUserInfo.getEncryptedData(),reqUserInfo.getIv());
                   ptpUserInfo=new PtpUserInfo();
                   ptpUserInfo.setUserPhone(userPhone);
                   ptpUserInfo.setWxOpenId(openid);
                   ptpUserInfo.setUserPwd(MD5Util.getMD5("123456"));
                   ptpUserInfo.setUserType(1);
                   ptpUserInfoService.save(ptpUserInfo);
               }
                String token = JwtTokenUtil.generateToken(ptpUserInfo);
                return GenericResponse.response(ServiceError.NORMAL,token);
            //通过手机号密码登陆
            }else if(StringUtils.isNoneEmpty(reqUserInfo.getUserPhone())&&StringUtils.isNoneEmpty(reqUserInfo.getUserPwd())) {
                QueryWrapper<PtpUserInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper
                        .eq("user_phone",reqUserInfo.getUserPhone())
                        .eq("user_pwd", MD5Util.getMD5(reqUserInfo.getUserPwd()))
                        .eq("user_type","2");
                PtpUserInfo ptpUserInfo= ptpUserInfoService.getOne(queryWrapper);
                if(ptpUserInfo!=null){
                    String token = JwtTokenUtil.generateToken(ptpUserInfo);
                    return GenericResponse.response(ServiceError.NORMAL,token);
                }
                return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_USER);
            //通过手机号验证码登陆
            }else if(StringUtils.isNoneEmpty(reqUserInfo.getUserPhone())&&StringUtils.isNoneEmpty(reqUserInfo.getValidateCode())){

            }

            return weChatService.wxLogin(reqUserInfo.getWxCode());
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
            redisUtill.delete(authHeader);
            return GenericResponse.response(ServiceError.NORMAL);
        }

        @PostMapping("/wx/getPhoneNum")
        public GenericResponse getPhoneNum( @RequestBody ReqGetPhoneNum reqGetPhoneNum)throws Exception{
//            return ptpUserInfoService.adminLogin(reqUserInfo);
            return null;
        }
    }
