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
import com.jca.datacommon.tool.CollectionUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.EmptyUtils;
import com.jca.weixinsmallprocess.service.visitor.TFVisitorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api("访客")
@RequestMapping("/visitor")
public class TFVisitorController {

	@Resource
	private TFVisitorService tFVisitorService;

	/**
	 * 所有访客记录
	 * 
	 * @param request
	 * @param propertyNo
	 * @return
	 */
	@ApiOperation(value = "所有访客记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/findAllVisitorInfo")
	public Result findAllVisitorInfo(HttpServletRequest request, @RequestParam(required = false) String propertyNo) {
		try {
			propertyNo = StringUtils.isEmpty(request.getParameter("propertyNo")) ? propertyNo
					: request.getParameter("propertyNo");
			List<TFVisitor> visitors = tFVisitorService.findAllVisitorRecord(propertyNo, null);
			if (CollectionUtils.isEmpty(visitors))
				return Result.error("暂无数据!");
			for (int i = 0; i < visitors.size(); i++) {
				TFVisitor v = visitors.get(i);
				v.setIndex((i + 1));
				v = deal(v);
				v.setConfuseReason(StringUtils.isEmpty(v.getConfuseReason())||EmptyUtils.isEmpty(v.getConfuseReason()) ? "- -" : v.getConfuseReason());
			}
			return Result.success().withData(visitors).withJsonSum(visitors.size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}
	/**
	 * 查看访客记录
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "查看访客记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/getOneVisitorInfo")
	public Result getOneVisitorInfo(HttpServletRequest request, @RequestParam(value = "id") String id) {
		try {
			List<TFVisitor> visitors = tFVisitorService.findAllVisitorRecord(null, id);
			visitors.get(0).setConfuseReason(StringUtils.isEmpty(visitors.get(0).getConfuseReason()) ? "- -"
					: visitors.get(0).getConfuseReason());
			deal(visitors.get(0));
			visitors.get(0).setVisitorPhoto(visitors.get(0).getVisitorPhoto().replace("\\\\", "\\"));
			return Result.success().withData(EmptyUtils.isEmpty(visitors) ? null : visitors.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	/**
	 * 审核
	 * 
	 * @param request
	 * @param id
	 * @param state
	 * @return
	 */
	@ApiOperation(value = "审核", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/checkVisitorInfo")
	public Result checkVisitorInfo(HttpServletRequest request,@ApiParam("编号") @RequestParam(value = "id",required=true) String id,
			@ApiParam("状态值") @RequestParam(value = "state") String state,@ApiParam("缘由")@RequestParam(value = "reason",required=false) String reason) {
		try {
			if(StringUtils.isEmpty(id)||StringUtils.isEmpty(state))
				return Result.error("必填字段");
			TFVisitor visitors = tFVisitorService.updateVisitor(id,state,reason);
			deal(visitors);
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
			visitor.setCheckState("已通过");
			break;
		case 2:
			visitor.setCheckState("已拒绝");
			break;
		case 3:
			visitor.setCheckState("通过");
			break;
		default:
			visitor.setCheckState("未审核");
			break;
		}
		return visitor;
	}

}
