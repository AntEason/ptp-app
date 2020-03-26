package com.ant.ptpapp.aspect;

import com.alibaba.fastjson.JSON;
import com.ant.ptpapp.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
@Slf4j
public class LogRecordAspect {

    private static final ThreadLocal<Long> START_TTIME_THREAD_LOCAL = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Pointcut("execution(* com.ant.ptpapp.controller..*.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long beginTime = System.currentTimeMillis();
        START_TTIME_THREAD_LOCAL.set(beginTime);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip= IpUtils.getClientIpAddress(request);
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();

        String params = "";
        //获取请求参数集合并进行遍历拼接
        if(args.length>0){
            if("POST".equals(method)){
                Map map = null;
                for (Object arg : args) {
                    map=new HashMap();
                    map.putAll(getKeyAndValue(arg));
                }
                if(!map.isEmpty()&&map.get("response")==null){
                    params = JSON.toJSONString(map);
                }
            }else if("GET".equals(method)){
                params = queryString;
            }
        }


        log.info("请求开始===地址:   {}\t",url);
        log.info("请求开始===IP地址: {}\t",ip);
        log.info("请求开始===类型:   {}\t",method);
        log.info("请求开始===参数:   {}\t",params);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        log.info("请求结束===返回值:   {}\t" , JSON.toJSONString(result));
         beginTime = START_TTIME_THREAD_LOCAL.get();
        long endTime = System.currentTimeMillis();
        log.info("请求结束===耗时:{}ms \t最大内存:{}m\t已分配内存:{}m\t已分配内存中的剩余空间:{}m\t最大可用内存:{}m",
                (endTime - beginTime), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        return result;
    }

    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            // 设置些属性是可以访问的
            f.setAccessible(true);
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                // 设置键值
                map.put(f.getName(), val);
            } catch (IllegalArgumentException e) {
                log.error("getKeyAndValue error - {}",e);
            } catch (IllegalAccessException e) {
                log.error("getKeyAndValue error - {}",e);
            }

        }
        return map;
    }
}
