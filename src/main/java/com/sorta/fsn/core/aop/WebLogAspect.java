package com.sorta.fsn.core.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author benju.xie
 * @version 0.0.1
 * @datetime 2017/5/2 15:19
 */
@Aspect
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static ObjectMapper objectMapper = new ObjectMapper();
    private  final String logPoint="@annotation(org.springframework.web.bind.annotation.RequestMapping)";
    @Pointcut(logPoint)
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        //获取所有参数：
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            logger.info(paraName + ": " + request.getParameter(paraName));
        }

    }


    @AfterReturning(value = "webLog()",returning = "returnValue")
    public void doAfterReturning(JoinPoint joinPoint,Object returnValue) {
        // 处理完请求，返回内容
        if(returnValue!=null) {
            try {
                logger.info(objectMapper.writeValueAsString(returnValue));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
}
