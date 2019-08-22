package com.jca.jurisdictionmanage.aop;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.Test;
import com.jca.datacommon.SystemLog;
import com.jca.datacommon.annotation.Log;
import com.jca.jurisdictionmanage.service.logservice.SystemLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dd
 * @E-mail: email
 * @version
 * @desc 切面类
 */
@Slf4j
@Aspect
@Component // 定义组件
public class SystemLogAspect {// implements Ordered

	// 注入日志Service用于把日志保存数据库
	@Resource
	private SystemLogService systemLogService;

	// private static final Logger log =
	// LoggerFactory.getLogger(SystemLogAspect.class);

	// Controller层切点
	@Pointcut("execution (* com.jca.peoplemanage.controller.*.*(..))")
	public void controllerAspect() {
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		System.out.println("==========开始执行controller前置通知===============");
		if (log.isInfoEnabled()) {
			log.info("before " + joinPoint);
		}
		System.out.println("==========执行controller前置通知结束===============");
	}

	// 配置controller环绕通知,使用在方法aspect()上注册的切入点
	// @Around("controllerAspect()")
	public void around(JoinPoint joinPoint) {
		System.out.println("==========开始执行controller环绕通知===============");
		long start = System.currentTimeMillis();
		try {
			((ProceedingJoinPoint) joinPoint).proceed();
			long end = System.currentTimeMillis();
			if (log.isInfoEnabled()) {
				log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
			}
			System.out.println("==========结束执行controller环绕通知===============");
		} catch (Throwable e) {
			long end = System.currentTimeMillis();
			if (log.isInfoEnabled()) {
				log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : "
						+ e.getMessage());
			}
		}
	}

	/**
	 * 后置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@After(value = "controllerAspect()")
	public void after(JoinPoint joinPoint) {

		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.getRequestAttributes()).getRequest(); HttpSession
		 * session = request.getSession();
		 */
		// 读取session中的用户
		// User user = (User) session.getAttribute("user");
		// 请求的IP
		// String ip = request.getRemoteAddr();
		Test user = new Test();
		user.setInfoId(1);
		user.setInfoName("dddd");
		String ip = "127.0.0.1";
		try {

			String targetName = joinPoint.getTarget().getClass().getName();
			log.info("目标名========》" + targetName);
			String methodName = joinPoint.getSignature().getName();
			log.info("方法名========》" + methodName);
			Object[] arguments = joinPoint.getArgs();
			@SuppressWarnings("rawtypes")
			Class targetClass = Class.forName(targetName);
			log.info("目标对象的class实例========》" + targetClass);
			Method[] methods = targetClass.getMethods();
			String operationType = "";
			String operationName = "";
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					@SuppressWarnings("rawtypes")
					Class[] clazzs = method.getParameterTypes();
					if (clazzs.length == arguments.length) {
						operationType = method.getAnnotation(Log.class).operationType();
						operationName = method.getAnnotation(Log.class).operationName();
						break;
					}
				}
			}
			// *========控制台输出=========*//
			System.out.println("=====controller后置通知开始=====");
			log.info("请求方法:"
					+ (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")
					+ "." + operationType);
			log.info("方法描述:" + operationName);
			log.info("请求人:" + user.getInfoName());
			log.info("请求IP:" + ip);

			// *========数据库日志=========*//
			System.out.println("=====开始输出到数据库=====");
			SystemLog logeEtity = new SystemLog();
			logeEtity.setId(UUID.randomUUID().toString());
			logeEtity.setDescription(operationName);
			logeEtity.setMethod(
					(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "."
							+ operationType);
			logeEtity.setLogType((long) 0);
			logeEtity.setRequestIp(ip);
			logeEtity.setExceptioncode(null);
			logeEtity.setExceptionDetail(null);
			logeEtity.setParams(null);
			logeEtity.setCreateBy(user.getInfoName());
			logeEtity.setCreateDate(LocalDateTime.now());
			// 保存日志数据库
			systemLogService.insert(logeEtity);
			System.out.println("=====输出到数据库结束=====");
			System.out.println("=====controller后置通知结束=====");
		} catch (Exception e) {
			// 记录本地异常日志
			log.error("==后置通知异常==");
			log.error("异常信息:{}");
			e.printStackTrace();
		}
	}

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning(value = "controllerAspect()", returning = "rtv") // Object为返回值
	public void doAfterReturning(JoinPoint joinPoint, Object rtv) {
		System.out.println("=====执行controller后置返回通知=====");
		if (log.isInfoEnabled()) {
			log.info("afterReturn " + joinPoint);
			String strLog = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
			log.info(strLog + "] [结束]" + "[Return:" + rtv + "]");
		}
	}

	/**
	 * 异常通知 用于拦截记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	// @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.getRequestAttributes()).getRequest(); HttpSession
		 * session = request.getSession(); //读取session中的用户 User user = (User)
		 * session.getAttribute(WebConstants.CURRENT_USER); //获取请求ip String ip =
		 * request.getRemoteAddr();
		 */
		// 获取用户请求方法的参数并序列化为JSON格式字符串

		Test user = new Test();
		user.setInfoId(1);
		user.setInfoName("dddd");
		String ip = "127.0.0.1";

		String params = "";
		if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";"; // JsonUtil.getJsonStr()
			}
		}
		try {

			String targetName = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] arguments = joinPoint.getArgs();
			Class targetClass = Class.forName(targetName);
			Method[] methods = targetClass.getMethods();
			String operationType = "";
			String operationName = "";
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Class[] clazzs = method.getParameterTypes();
					if (clazzs.length == arguments.length) {
						operationType = method.getAnnotation(Log.class).operationType();
						operationName = method.getAnnotation(Log.class).operationName();
						break;
					}
				}
			}
			/* ========控制台输出========= */
			System.out.println("=====异常通知开始=====");
			System.out.println("异常代码:" + e.getClass().getName());
			System.out.println("异常信息:" + e.getMessage());
			System.out.println("异常方法:"
					+ (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")
					+ "." + operationType);
			System.out.println("方法描述:" + operationName);
			System.out.println("请求人:" + user.getInfoName());
			System.out.println("请求IP:" + ip);
			System.out.println("请求参数:" + params);
			/* ==========数据库日志========= */
			SystemLog log = new SystemLog();
			log.setId(UUID.randomUUID().toString());
			log.setDescription(operationName);
			log.setExceptioncode(e.getClass().getName());
			log.setLogType((long) 1);
			log.setExceptionDetail(e.getMessage());
			log.setMethod(
					(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			log.setParams(params);
			log.setCreateBy(user.getInfoName());
			log.setCreateDate(LocalDateTime.now());
			log.setRequestIp(ip);
			// 保存数据库
			systemLogService.insert(log);
			System.out.println("=====异常通知结束=====");
		} catch (Exception ex) {
			// 记录本地异常日志
			log.error("==异常通知异常==");
			log.error("异常信息:{}", ex.getMessage());
		}
		/* ==========记录本地异常日志========== */
		log.error("异常方法:{}异常代码:{}异常信息:{}参数:{}",
				joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(),
				e.getMessage(), params);

	}
}