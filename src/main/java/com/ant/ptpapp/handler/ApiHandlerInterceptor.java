package com.ant.ptpapp.handler;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.util.RequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ApiHandlerInterceptor implements HandlerInterceptor {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-startTimed");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("##############################【一个MVC完整请求开始】##############################");

        log.info("*******************MVC业务处理开始**********************");
        try {
            long timed = System.currentTimeMillis();
            startTimeThreadLocal.set(timed);

            String requestURL = request.getRequestURI();
            log.info("当前请求的URL：【{} 】", requestURL);
            log.info("执行目标方法: {}", handler);

            Map<String, ?> params = request.getParameterMap();
            log.info("当前请求参数打印：");
            if (!params.isEmpty()) {
                print(request.getParameterMap(),"参数");
            }
           String json= new RequestWrapper(request).getBodyString();
            if(StringUtils.isNoneEmpty(json)){
                log.info("参数 : {}",json);
            }
        } catch (Exception e) {
            log.error("MVC业务处理-拦截器异常：", e);
        }
        log.info("*******************MVC业务处理结束**********************");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("*******************一个MVC 视图渲染开始**********************");

        try {
            log.info("执行业务逻辑代码耗时：【{} ms】",  (System.currentTimeMillis() - startTimeThreadLocal.get()));
            String requestURL = request.getRequestURI();
            log.info("当前请求的URL：【{}】", requestURL);

            if (modelAndView != null) {
                log.info("即将返回到MVC视图：{}", modelAndView.getViewName());

                if (modelAndView.getView() != null) {
                    log.info("返回到MVC视图内容类型ContentType：{}", modelAndView.getView().getContentType());
                }

                if (!modelAndView.getModel().isEmpty()) {

                    log.info("返回到MVC视图{}数据打印如下：", modelAndView.getViewName());
                    print(modelAndView.getModel(), "返回数据");
                }
            }
        } catch (Exception e) {
            log.error("MVC 视图渲染-拦截器异常：", e);
        }

        log.info("*******************一个MVC 视图渲染结束**********************");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            String requestURL = request.getRequestURI();
            log.info("MVC返回请求完成URL：【{}】", requestURL);
            log.info("MVC返回请求完成耗时：【{} ms】", (System.currentTimeMillis() - startTimeThreadLocal.get()));
            if (ex != null) {
                log.info("MVC返回请求发生异常：", ex.getMessage());
                log.error("异常信息如下：", ex);
            }
        } catch (Exception e) {
            log.error("MVC完成返回-拦截器异常：", e);
        }

        log.info("##############################【一个MVC完整请求完成】##############################");
    }

    private void print(Map<String, ?> map, String prefix) {
        if (map != null) {
            Set<String> keys = map.keySet();
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {

                String name = iter.next();
                if (name.contains("org.springframework.validation.BindingResult")) {
                    continue;
                }

                String value = "";
                try {
                    value = MAPPER.writeValueAsString(map.get(name));
                } catch (Exception e) {
                    log.error("转换参数【{}】发生异常：", name, e);
                }
                log.info("{} \"{}\"： {}", prefix, name, value);
            }
        }
    }
}
