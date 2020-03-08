package com.ant.ptpapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.User;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.service.WeChatService;
import com.ant.ptpapp.util.Jcode2SessionUtil;
import com.ant.ptpapp.util.JwtTokenUtil;
import com.ant.ptpapp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 微信业务实现类
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Value("${weChat.appid}")
    private String appid;

    @Value("${weChat.secret}")
    private String secret;

    @Value("${weChat.width}")
    private Integer width;

    @Value("${weChat.autoColor}")
    private boolean  autoColor;

    @Value("${weChat.isHyaline}")
    private boolean isHyaline;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public GenericResponse wxLogin(String code) throws Exception{
//        JSONObject sessionInfo = JSONObject.parseObject(jcode2Session(code));
//        log.info("sessionInfo ==="+sessionInfo.toJSONString());
//        Assert.notNull(sessionInfo,"code 无效");
//
//        Assert.isTrue(!StringUtils.isEmpty(sessionInfo.getString("openid")),"open_id为空");
////
//        // 获取用户唯一标识符 openid成功
//        // 模拟从数据库获取用户信息
//        User user = new User();
//        user.setId(1L);
//        Set authoritiesSet = new HashSet();
//        // 模拟从数据库中获取用户权限
//        authoritiesSet.add(new SimpleGrantedAuthority("test:add"));
//        authoritiesSet.add(new SimpleGrantedAuthority("test:list"));
//        authoritiesSet.add(new SimpleGrantedAuthority("ddd:list"));
//        user.setAuthorities(authoritiesSet);
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("id",user.getId().toString());
//        hashMap.put("authorities",authoritiesSet);
//        String token = JwtTokenUtil.generateToken(user);
//        redisUtil.hset(token,hashMap);
//
//        return GenericResponse.response(ServiceError.NORMAL,token);
        return null;
    }

    @Override
    public String getAccessToken() {
        JSONObject sessionInfo = JSONObject.parseObject(Jcode2SessionUtil.getAccessToken(appid,secret));
        Assert.notNull(sessionInfo,"code 无效");
        String accessToken=(String)redisUtil.get("accessToken");
        if(StringUtils.isEmpty(accessToken)){
             accessToken=sessionInfo.getString("access_token");
            redisUtil.set("accessToken",accessToken,7200);
        }
        return accessToken;
    }

    @Override
    public boolean createQRCode(String scene,String fileName) {
       String token= getAccessToken();
        return Jcode2SessionUtil.getUnlimited(token,scene,null,width,autoColor,fileName,isHyaline);
    }

    /**
     * 登录凭证校验
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public String jcode2Session(String code)throws Exception{
        String sessionInfo = Jcode2SessionUtil.jscode2session(appid,secret,code,"authorization_code");//登录grantType固定
        return sessionInfo;
    }
}
