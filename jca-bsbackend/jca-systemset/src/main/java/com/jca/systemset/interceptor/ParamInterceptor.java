package com.jca.systemset.interceptor;

import java.lang.reflect.Field;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;

public class ParamInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ParamInterceptor.class);

	private static final String USER_PARAM = "usertoken";

	private static final String USER_HEADER = "userName";
	@Resource
	private RedisAPI redisAPI;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("进入拦截器==>");
		String usertoken = request.getParameter(USER_PARAM);
		System.out.println(usertoken);
		if (EmptyUtils.isEmpty(usertoken))
			return false;
		TFOperator operator = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		request.setAttribute(operator.getOperatorName(), operator);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("拦截结束==>");
		super.afterCompletion(request, response, handler, ex);
	}
	/**
	 * 反射设置头部
	 * @param request
	 * @param key
	 * @param value
	 */
	private void reflectSetHeader(HttpServletRequest request, String key, String value){
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        logger.info("request实现类={}", requestClass.getName());
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
            o2.removeHeader(key);
            o2.addHeader(key, value);
        } catch (Exception e) {
            logger.info("reflect set header error {}", e);
        }
    }
}