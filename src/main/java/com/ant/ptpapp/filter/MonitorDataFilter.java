package com.ant.ptpapp.filter;

import com.alibaba.fastjson.JSONObject;
import com.ant.ptpapp.util.RequestWrapper;
import com.ant.ptpapp.util.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述:
 *
 * @author yichen
 * @create 2020-03-12 10:55 下午
 */
@Component
@WebFilter(urlPatterns = "/**", filterName = "monitorDataFilter")
@Slf4j
public class MonitorDataFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper(httpServletResponse);
        chain.doFilter(requestWrapper, responseWrapper);
        byte[] bytes = responseWrapper.getBytes();
        String val = new String(bytes, "UTF-8");
        log.info("响应数据 : {}",val);
        //将数据 再写到 response 中
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, responseWrapper);
        }
        String result = new String(val);
        //解决可能在运行的过程中页面只输出一部分
        response.setContentLength(-1);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(result);
        out.flush();
        out.close();
    }
}
