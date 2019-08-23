package com.jca.weixinsmallprocess.controller.visitor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.weixinsmallprocess.service.visitor.TFVisitorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api("访客")
@RequestMapping("/visitor")
public class TFVisitorController {

	@Resource
	private TFVisitorService tFVisitorService;

	@ApiOperation(value = "所有访客记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/findAllVisitorInfo")
	public Result findAllVisitorInfo(HttpServletRequest request, @RequestParam(required = false) String propertyNo) {
		try {
			propertyNo = StringUtils.isEmpty(request.getParameter("propertyNo")) ? propertyNo
					: request.getParameter("propertyNo");
			List<TFVisitor> visitors = tFVisitorService.findAllVisitorRecord(propertyNo, null);
			visitors.stream().forEach(v -> {
				v = deal(v);
			});
			return Result.success().withData(visitors).withJsonSum(visitors.size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	@ApiOperation(value = "查看访客记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/getOneVisitorInfo")
	public Result getOneVisitorInfo(HttpServletRequest request, @RequestParam(value = "id") Integer id) {
		try {
			List<TFVisitor> visitors = tFVisitorService.findAllVisitorRecord(null, id);
			return Result.success().withData(visitors.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}
	
	@ApiOperation(value = "审核", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/checkVisitorInfo")
	public Result checkVisitorInfo(HttpServletRequest request, @RequestParam(value = "id") Integer id) {
		try {
			TFVisitor visitors = tFVisitorService.updateById(null);
			return Result.success().withData(visitors);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	public TFVisitor deal(TFVisitor visitor) {
		switch (Integer.valueOf(visitor.getCheckState())) {
		case 1:
			visitor.setCheckState("已审核");
			break;
		case 2:
			visitor.setCheckState("已拒绝");
			break;
		case 3:
			visitor.setCheckState("通过");
			break;
		default:
			visitor.setCheckState("待审核");
			break;
		}
		if(StringUtils.isEmpty(visitor.getVisitorReason()))
			visitor.setVisitorReason("--");
		return visitor;
	}

}
