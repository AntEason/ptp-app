package com.ant.ptpapp.filter;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.common.GenericResponse;
import com.ant.ptpapp.common.ServiceError;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.ant.ptpapp.entity.User;
import com.ant.ptpapp.service.SelfUserDetailsService;
import com.ant.ptpapp.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * @author: yichen
 * @description: 确保在一次请求只通过一次filter，而不需要重复执行
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${token.expirationMilliSeconds}")
    private long expirationMilliSeconds;

    @Autowired
    SelfUserDetailsService userDetailsService;

    @Autowired
    RedisUtil redisUtil;


    static final String ORIGIN = "Origin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = new RequestWrapper(httpServletRequest);
        String origin = request.getHeader(ORIGIN);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "content-type, authorization");
        String authHeader = request.getHeader("Authorization");

        response.setCharacterEncoding("utf-8");
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")){
            //token格式不正确
            filterChain.doFilter(requestWrapper,response);
            return;
        }
        String authToken = authHeader.substring("Bearer ".length());
        //获取在token中自定义的subject，用作用户标识，用来获取用户权限
        String subject = JwtTokenUtil.parseToken(authToken);
        log.info(" Authorization ======"+authHeader);
        //获取redis中的token信息

        if (StringUtils.isEmpty((String)redisUtil.get(authToken))){
            //token 不存在 返回错误信息
            write(JSON.toJSONString(GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN)),response);
            return;
        }

        String json= (String) redisUtil.get(authToken);
        if (StringUtils.isEmpty(json)){
            //用户信息不存在或转换错误，返回错误信息
            write(JSON.toJSONString(GenericResponse.response(ServiceError.GLOBAL_ERR_NO_SIGN_IN)),response);
            return;
        }
        PtpUserInfo ptpUserInfo=JSON.parseObject(json,PtpUserInfo.class);

        //更新token过期时间
        redisUtil.setKeyExpire(authToken,expirationMilliSeconds);
        //将信息交给security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(ptpUserInfo,null,null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("================进入过滤器======================");
        // 防止流读取一次后就没有了, 所以需要将流继续写出去

        filterChain.doFilter(requestWrapper,response);
    }


    private void write(String json,HttpServletResponse response){
        OutputStream outputStream=null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}