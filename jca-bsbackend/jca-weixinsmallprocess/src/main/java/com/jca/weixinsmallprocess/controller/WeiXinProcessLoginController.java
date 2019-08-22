package com.jca.weixinsmallprocess.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TDEmployinfo;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.web.form.SmallProcessLoginForm;
import com.jca.datatool.CacheUtil;
import com.jca.weixinsmallprocess.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api("微信信息")
@Slf4j
@RestController
@RequestMapping("WeiXin/Login")
public class WeiXinProcessLoginController {

	@Resource
	private AdminService adminService;

	@ApiOperation(value = "小程序登录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/smallProcessLogin", method = RequestMethod.POST)
	public Result smallProcessLogin(@RequestParam(value="json",required=true) String loginFormVo) {
		try {
			SmallProcessLoginForm loginForm = JSON.parseObject(loginFormVo,SmallProcessLoginForm.class);
			TFEmployInfo operator = null;
			Map resultMap = adminService.login(TFEmployInfo.builder().telephone(loginForm.getTelephone()).build());
			if (resultMap.containsKey("weiXinOperator")) {
				operator = (TFEmployInfo) resultMap.get("weiXinOperator");
				String endCall = loginForm.getTelephone().substring(7);
				if ((endCall.length() != loginForm.getPassword().length())
						|| ((endCall.length() == loginForm.getPassword().length())
								&& !loginForm.getPassword().equals(endCall)))
					return Result.error("密码错误!");
			} else {
				return Result.error("账号错误!");
			}
			CacheUtil.cache.put("weiXinOperator-" + operator.getEmployId(), operator);
			return Result.success("登录成功!").withData(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}
}
