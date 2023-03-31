package com.zjun122.aspect;

import com.alibaba.fastjson.JSON;
import com.zjun122.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.zjun122.annotation.SystemLog)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res;
        try {
            handlerBefore(joinPoint);
            res = joinPoint.proceed();
            handlerAfter(res);
        } finally {
            log.info("=======End=======" + System.lineSeparator());
        }

        return res;
    }

    private void handlerAfter(Object res) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(res));
    }

    private void handlerBefore(ProceedingJoinPoint joinPoint) {
        //获取HttpServletRequest对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ///获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.BusinessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //获取注解对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }
}
