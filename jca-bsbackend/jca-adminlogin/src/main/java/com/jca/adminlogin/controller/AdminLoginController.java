package com.jca.adminlogin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.adminlogin.service.AdminService;
import com.jca.adminlogin.service.permission.TFMenuPermissionService;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datacommon.web.form.AdminLoginForm;
import com.jca.datacommon.web.form.UpdatePassWordForm;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录控制器
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes" })
@Slf4j
@Api(value = "登录管理")
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

	@Autowired
	private TFMenuPermissionService permissionService;
	@Resource
	private AdminService adminService;
	@Resource
	private RedisAPI redisAPI;

	/**
	 * 登录
	 * 
	 * @param loginFormVo
	 * @return
	 */
	@ApiOperation(value = "登录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Result login(@RequestParam("json") String loginFormVo, HttpServletRequest request) {
		try {
			AdminLoginForm loginForm = JSON.parseObject(loginFormVo, AdminLoginForm.class);
			long start = System.currentTimeMillis();
			TFOperator operator = null;
			Map resultMap = adminService.login(TFOperator.builder().operatorNo(loginForm.getUsername()).build());
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			if (resultMap.containsKey("operator")) {
				operator = (TFOperator) resultMap.get("operator");
				if (!operator.getPassword().equals(loginForm.getPassword()))
					return Result.error("密码错误!");
			} else {
				return Result.error("账号错误!");
			}
			log.info("远程URI==>" + request.getRequestURI());
			// 缓存用户
			CacheUtil.cache.put("operator-" + operator.getOperatorNo(), operator);
			return Result.success("登录成功!").withData(resultMap);
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param passWordForm
	 * @return
	 */
	@ApiOperation(value = "修改密码", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/updatePassWord")
	public Result updatePassWord(@RequestParam(value = "json") String passWordForm) {
		try {
			long start = System.currentTimeMillis();
			UpdatePassWordForm wordForm = JSON.parseObject(passWordForm, UpdatePassWordForm.class);
			TFOperator checkOp = adminService.getById(wordForm.getOperatorId());
			if (!checkOp.getPassword().equals(wordForm.getOldPassword()))
				return Result.error("原密码不正确!");
			long end = System.currentTimeMillis();
			TFOperator operator = adminService.updatePassWord(
					TFOperator.builder().operatorId(wordForm.getOperatorId()).password(wordForm.getPassword()).build());
			log.info("耗时==>" + (end - start));
			if (EmptyUtils.isNotEmpty(operator))
				return Result.success("修改成功!").withData(operator);
			return Result.error("修改失败!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("操作错误!").withData(e);
		}
	}

	/**
	 * 获取菜单信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取菜单信息信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/getMenus", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public Result getMenus(HttpServletRequest request, @RequestParam(required = false) String token) {
		try {
			TFOperator operator = null;
			String usertoken = request.getHeader("usertoken");
			usertoken = StringUtils.isEmpty(request.getHeader("usertoken")) ? token : request.getHeader("usertoken");
			if (EmptyUtils.isNotEmpty(usertoken)) {
				log.info("剩余有效时间==>" + redisAPI.ttl(usertoken));
				if (redisAPI.ttl(usertoken) == -2)
					return Result.error("登录已过期，即将返回登录界面!");
				operator = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
				if (redisAPI.ttl(usertoken) == -1)
					redisAPI.set(usertoken, 30 * 60, JSON.toJSONString(operator));
				log.info("当前操作用户==>" + operator.getOperatorName());
				return Result.success().withData(permissionService.saveIdAndPermission(operator.getOperatorId()));
			}
			return Result.error("token为空!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("异常!");
		}
	}

	@RequestMapping(value = "hello")
	public String hello() {
		return "hello!";
	}
}
