package com.ant.ptpapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.req.ReqUserInfo;
import com.ant.ptpapp.mapper.PtpUserInfoMapper;
import com.ant.ptpapp.service.PtpUserInfoService;
import com.ant.ptpapp.util.JwtTokenUtil;
import com.ant.ptpapp.util.MD5Util;
import com.ant.ptpapp.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yichen
 * @since 2020-02-26
 */
@Service
public class PtpUserInfoServiceImpl extends ServiceImpl<PtpUserInfoMapper, PtpUserInfo> implements PtpUserInfoService {
    @Autowired
    PtpUserInfoMapper ptpUserInfoMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public GenericResponse adminLogin(ReqUserInfo reqUserInfo) {
        QueryWrapper<PtpUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_phone", reqUserInfo.getUserPhone())
                .eq("user_pwd", MD5Util.getMD5(reqUserInfo.getUserPwd()))
                .eq("user_type", "2");
        PtpUserInfo ptpUserInfo = ptpUserInfoMapper.selectOne(queryWrapper);
        if (ptpUserInfo != null) {
            String token = JwtTokenUtil.generateToken(ptpUserInfo);
            redisUtil.set(token, JSON.toJSONString(ptpUserInfo));
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userPhone", reqUserInfo.getUserPhone());
            return GenericResponse.response(ServiceError.NORMAL, result);
        } else{
            return GenericResponse.response(ServiceError.GLOBAL_ERR_NO_USER);
        }
    }
}
