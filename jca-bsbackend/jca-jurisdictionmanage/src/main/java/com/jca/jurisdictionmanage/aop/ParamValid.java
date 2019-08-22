package com.jca.jurisdictionmanage.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.jca.datacommon.aop.validator.AopParamValidator;
import com.jca.datacommon.exception.ParamValidErrorException;
import com.jca.datacommon.tool.Result;

/**
 * 参数校验切面
 * @author 
 * @version 1.0
 * @date 
 */
@Aspect
@Component
public class ParamValid {

    @Pointcut(value = "execution(* com.jca.peoplemanage.controller..*Controller.*(..))")
    private void controller() {}

    /**
     *
     */
    @Pointcut("@args(com.jca.datacommon.annotation.Valid)")
    private void validArgs() {}

    @Around("controller()")
    public Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        Object[] args = joinPoint.getArgs();
        try {
            AopParamValidator.getInstance().validate(method, args);
        } catch (ParamValidErrorException e) {
            return Result.paramError(e.getMessage());
        }
        return joinPoint.proceed();
    }
}
