package com.jca.weixinsmallprocess.controller.ownerapply;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jca.databeans.pojo.TFOwnerApply;
import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.tool.CollectionUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.EmptyUtils;
import com.jca.weixinsmallprocess.controller.visitor.TFVisitorController;
import com.jca.weixinsmallprocess.service.ownerapply.OwnerApplyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api("业主申请")
@RequestMapping("/ownerapply")
public class OwnerApplyController {

	@Resource
	private OwnerApplyService ownerApplyService;

	@ApiOperation(value = "所有业主申请记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/findAllOwnerApplyInfo")
	public Result findAllOwnerApplyInfo(HttpServletRequest request, @RequestParam(required = false) String propertyNo) {
		try {
			propertyNo = StringUtils.isEmpty(request.getParameter("propertyNo")) ? propertyNo
					: request.getParameter("propertyNo");
			List<TFOwnerApply> visitors = ownerApplyService.findAllOwnerApply(propertyNo);
			if (CollectionUtils.isEmpty(visitors))
				return Result.error("暂无数据!");
			for (int i = 0; i < visitors.size(); i++) {
				TFOwnerApply v = visitors.get(i);
				v.setIndex((i + 1));
				v = deal(v);
				v.setOwnerConfuseReason(
						StringUtils.isEmpty(v.getOwnerConfuseReason()) || EmptyUtils.isEmpty(v.getOwnerConfuseReason())
								? "- -"
								: v.getOwnerConfuseReason());
			}
			return Result.success().withData(visitors).withJsonSum(visitors.size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	public TFOwnerApply deal(TFOwnerApply visitor) {
		switch (Integer.valueOf(visitor.getOwnerCheckState())) {
		case 1:
			visitor.setOwnerCheckState("已通过");
			break;
		case 2:
			visitor.setOwnerCheckState("已拒绝");
			break;
		case 3:
			visitor.setOwnerCheckState("通过");
			break;
		default:
			visitor.setOwnerCheckState("未审核");
			break;
		}
		return visitor;
	}
}
