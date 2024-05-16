package com.news.ai.gather.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author zhiweicoding.xyz
 * @date 5/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAop {

    @Pointcut("@annotation(com.news.ai.gather.annotation.EnableLogAnnotation)")
    public void apiPointCut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void pagePointCut() {
    }

    @Around("apiPointCut() && pagePointCut()")
    public Object logRequest(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        log.debug("请求方法：{}.{}", method.getDeclaringClass().getName(), method.getName());
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            log.debug("参数名：{}，参数值：{}", parameter.getName(), args[i]);
        }
        Object res = null;
        try {
            res = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
