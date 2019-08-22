package com.jca.jurisdictionmanage.aop;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jca.datacommon.log.LogHandler;
import com.jca.datacommon.log.LogInfo;
import com.jca.datacommon.log.LogResult;
import com.jca.datacommon.log.MethodLog;

/**
 * @author
 * @version 1.0
 * @description: 日志切面
 * @date
 */
@Aspect
@Component
public class LogAspect {

	private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

	private LogHandler handler = LogHandler.getInstance();

	/**
	 * 拦截带有@MethodLog的方法或带有该注解的类
	 */
	@Pointcut("@annotation(com.jca.datacommon.log.MethodLog) || @within(com.jca.datacommon.log.MethodLog)")
	private void logPointcut() {
	}

	@Before("logPointcut()")
	public void before(JoinPoint joinPoint) {
		LOG.info("前置增强开始==》");
		LOG.info("前置增强结束==》");
	}

	@AfterThrowing(pointcut = "logPointcut()", throwing = "e")
	public void doException(JoinPoint jp, Exception e) {
		LOG.error("===>出现异常：");
		Object[] args = jp.getArgs();
		Method method = ((MethodSignature) jp.getSignature()).getMethod();
		LogInfo logInfo = handler.getLogInfo(method);
		LOG.error(logInfo.getExceptionLogMsg(LogResult.exception(args, e)));
	}

	@Around(value = "logPointcut()")
	private Object afterReturning(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		LogInfo logInfo = handler.getLogInfo(method);

		long startTime = System.currentTimeMillis();
		Object result = pjp.proceed();
		long endTime = System.currentTimeMillis();

		MethodLog.LogLevel sysLogLevel = getSysLogLevel();
		String logMsg = logInfo.fillLogMsg(sysLogLevel, new LogResult(args, result, endTime - startTime));
		if (logMsg == null) {
			return result;
		}
		LOG.info("所花时间==>" + (endTime - startTime));
		LOG.info("日志信息==>" + logMsg + "<==结束");
		switch (logInfo.getLevel()) {
		case DEBUG:
			LOG.debug(logMsg);
			break;
		case WARN:
			LOG.warn(logMsg);
			break;
		case INFO:
			LOG.info(logMsg);
			break;
		case ERROR:
			LOG.error(logMsg);
			break;
		default:
			LOG.info(logMsg);
			break;
		}

		return result;
	}

	/**
	 * 获取系统的日志级别
	 * 
	 * @return
	 */
	private MethodLog.LogLevel getSysLogLevel() {
		if (LOG.isDebugEnabled()) {
			return MethodLog.LogLevel.DEBUG;
		}
		if (LOG.isInfoEnabled()) {
			return MethodLog.LogLevel.INFO;
		}
		if (LOG.isWarnEnabled()) {
			return MethodLog.LogLevel.WARN;
		}
		if (LOG.isErrorEnabled()) {
			return MethodLog.LogLevel.ERROR;
		}
		return MethodLog.LogLevel.NONE;
	}
}
