package com.jca.datacommon.exception;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jca.datacommon.tool.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理机制
 * 
 * @author Administrator
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	//@ExceptionHandler用于全局处理控制器里的异常
	//@InitBinder：用来设置WebDataBinder，用于自动绑定前台请求参数到Model中。
	//@ModelAttribute 本来作用是绑定键值对到Model中，此处让全局的@RequestMapping都能获得在此处设置的键值对
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result handleException(Exception e) {
		log.info("处理异常开始==>");
		log.error("异常：", e);
		if (e instanceof BusinessException || e instanceof BusinessRuntimeException) {
			return Result.error(e.getMessage());
		}
		if (e instanceof HttpRequestMethodNotSupportedException) {
			return Result.serverError("url请求方法错误！");
		}
		if(e instanceof HttpMessageNotReadableException) {
			return Result.error("Required request body content is missing");
		}
		if(e instanceof ParseException) {
			return Result.error("date Parse Error");
		}
		if (e instanceof MethodArgumentNotValidException) {
			log.info("属于MethodArgumentNotValidException异常");
			MethodArgumentNotValidException ex=(MethodArgumentNotValidException)e;
			BindingResult result = ex.getBindingResult();
		    List<FieldError> fieldErrors = result.getFieldErrors();
		    if(result.hasFieldErrors())
		    	return Result.error("Not Vaild").withData(fieldErrors);
		}
		log.info("处理异常结束==>");
		return Result.serverError();
	}
	
	@ModelAttribute
	@ResponseBody
	public void addModelAttribute(Model andView) {
		andView.addAttribute("test", 10);
	}
	
	
}
