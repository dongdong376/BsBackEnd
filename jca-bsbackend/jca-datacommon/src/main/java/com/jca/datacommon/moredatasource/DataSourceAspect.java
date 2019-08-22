package com.jca.datacommon.moredatasource;



import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import com.jca.datacommon.MoreDataSource;
import com.jca.datacommon.annotation.DataSource;

import lombok.extern.slf4j.Slf4j;


/**
 * 数据源切面，实现前后增强
 * @author Administrator
 *
 */
@Slf4j
@Component
public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {
	
	/**
	 * 完成任务之后清空数据源
	 */
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		log.info("清空数据源");
		DataSourceContextHolder.clearDataSourceType();//https://www.cnblogs.com/liujiduo/p/5004691.html		
	}
	
	/**
	 * 在开始任务之前获取注解进行切换数据源
	 */
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		if (method.isAnnotationPresent(DataSource.class)) {
			DataSource datasource = method.getAnnotation(DataSource.class);
			DataSourceContextHolder.setDataSourceType(datasource.value());			
		} else {
			DataSourceContextHolder.setDataSourceType(MoreDataSource.jcafaceone.toString());			
		}
		log.info("切换到数据源"+DataSourceContextHolder.getDataSourceType());
	}
	
	 /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     * 
     * @param point
     * @throws Exception
     */
    public void intercept(JoinPoint point) throws Exception {
        Class<?> target = point.getTarget().getClass();
        //获取方法标识
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 默认使用目标类型的注解，如果没有则使用其实现接口的注解
        for (Class<?> clazz : target.getInterfaces()) {
            resolveDataSource(clazz, signature.getMethod());
        }
        resolveDataSource(target, signature.getMethod());
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     * 
     * @param clazz
     * @param method
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                DataSourceContextHolder.setDataSourceType(source.value());
            }
            // 方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                DataSourceContextHolder.setDataSourceType(source.value());
            }
        } catch (Exception e) {
            System.out.println(clazz + ":" + e.getMessage());
        }
    }
}