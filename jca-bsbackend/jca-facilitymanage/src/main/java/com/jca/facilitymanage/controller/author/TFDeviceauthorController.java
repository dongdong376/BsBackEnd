package com.jca.facilitymanage.controller.author;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFDevice;
import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.databeans.vo.AddDeviceauthorVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.CollectionUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.facilitymanage.controller.device.TFDeviceController;
import com.jca.facilitymanage.service.author.TFDeviceauthorService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/device/author")
public class TFDeviceauthorController {

	@Resource
	private TFDeviceauthorService tFDeviceauthorService;

	/**
	 * 查询设备人员授权信息
	 * 
	 * @param request
	 * @param param
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "查询所有设备授权信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "查询所有设备授权信息")
	@RequestMapping(value = "/allDeviceAuthor", produces = "application/json;charset=UTF-8")
	public Result allDeviceAuthor(HttpServletRequest request,
			@RequestParam(required = false, value = "param") String param,
			@RequestParam(required = false) String name) {
		try {
			long start = System.currentTimeMillis();
			if (!StringUtils.isEmpty(request.getParameter("propertyNo")))
				name = request.getParameter("propertyNo");
			List<TFDeviceauthor> tfDeviceauthors = tFDeviceauthorService.findAllAuthor(param, name);
			int i = 1;
			for (TFDeviceauthor tfDeviceauthor : tfDeviceauthors) {
				tfDeviceauthor.setIndex(i++);
			}
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(tfDeviceauthors.toArray());
		} catch (Exception e) {
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!").withData(e.getMessage());
		}
	}

	/**
	 * 添加授权信息
	 * 
	 * @param request
	 * @param deviceauthorParam
	 * @return
	 */
	@ApiOperation(value = "添加授权信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "添加授权信息")
	@RequestMapping(value = "/addAuthorInfo", produces = "application/json;charset=UTF-8")
	public Result addAuthorInfo(HttpServletRequest request, @RequestParam("empNos") String empNo,
			@RequestParam("faceNos") String faceNo) {
		try {
			long start = System.currentTimeMillis();
			if (StringUtils.isEmpty(empNo) || StringUtils.isEmpty(faceNo))
				return Result.error("请选择相关信息!");
			String empNos[] = empNo.split(",");
			String faceNos[] = faceNo.split(",");
			Result result = tFDeviceauthorService
					.insertAuthor(AddDeviceauthorVo.builder().faceSns(faceNos).employNos(empNos).build());
			if (result.getCode() == 400)
				return Result.error("授权失败!");
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!").withData(e.getMessage());
		}
	}

	@ApiOperation(value = "删除授权信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "删除授权信息")
	@RequestMapping(value = "/removeAuthorInfo", produces = "application/json;charset=UTF-8")
	public Result removeAuthorInfo(HttpServletRequest request, @RequestParam("ids") String id) {
		try {
			long start = System.currentTimeMillis();
			String ids[] = id.split(",");
				Integer result = tFDeviceauthorService.removeAuthor(ids);
				if(result<=0)
					return Result.error("删除失败!");						
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("删除成功!");
		} catch (Exception e) {
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!").withData(e.getMessage());
		}
	}
}
