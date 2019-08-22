package com.jca.facilitymanage.controller.area;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFArea;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.vo.AddAreaVo;
import com.jca.databeans.vo.UpdateAreaVo;
import com.jca.datacommon.annotation.Valid;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.facilitymanage.service.area.TFAreaService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/areaManage/area")
public class TFAreaController {

	@Resource
	private TFAreaService tFAreaService;
	@Resource
	private RedisAPI redisAPI;
	/**
	 * 查询所有设备区域
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询所有设备所在区域信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "查询所有设备所在区域信息")
	@RequestMapping(value = "/AlldeviceOfArea", produces = "application/json;charset=UTF-8")
	public Result AlldeviceOfArea(HttpServletRequest request,@RequestParam(required=false)String proNo) {
		try {
			String key = StringUtils.isEmpty(request.getParameter("propertyNo"))?proNo:request.getParameter("propertyNo");
			long start = System.currentTimeMillis();
			List<TFArea> devices = tFAreaService.findAllArea(key);
			int i = 1;
			for (TFArea tfArea : devices) {
				tfArea.setIndex(i);
				i++;
			}
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(devices);
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	/**
	 * 删除设备区域
	 * 
	 * @param idsParam
	 * @return
	 */
	@ApiOperation(value = "删除设备区域信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "删除设备区域信息")
	@RequestMapping(value = "/removeAreaInfo", produces = "application/json;charset=UTF-8")
	public Result removeAreaInfo(@RequestParam("ids") String idsParam) {
		try {
			long start = System.currentTimeMillis();
			String ids[] = idsParam.split(",");
			Result result = checkAreaUnderDevice(ids);
			if (result.getCode() != 200)
				return result;
			Integer removeResult = tFAreaService.removeAreaByArray(ids);
			if (removeResult > 0)
				return Result.success("删除成功!");
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("删除失败!");
		} catch (Exception e) {
			log.error("error:" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	/**
	 * 添加设备区域
	 * 
	 * @param addAreaVo
	 * @return
	 */
	@ApiOperation(value = "添加设备区域信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "添加设备区域信息")
	@RequestMapping(value = "/AddAreaInfo", produces = "application/json;charset=UTF-8")
	public Result AddAreaInfo(@RequestParam("json") String addAreaVoParam,HttpServletRequest request) {
		try {
			String usertoken=request.getHeader("usertoken");
			if(EmptyUtils.isEmpty(usertoken))
				return Result.error("还未登陆!");
			TFOperator currentUser=JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if(EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			AddAreaVo addAreaVo = JSON.parseObject(addAreaVoParam, AddAreaVo.class);
			System.out.println(addAreaVo.getPropertyKey());
			long start = System.currentTimeMillis();
			TFArea area = new TFArea();
			area.setAreaName(addAreaVo.getAreaName());
			area.setAttributes(addAreaVo.getAttributes());
			area.setCreateBy(String.valueOf(currentUser.getOperatorId()));
			area.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			area.setProvince(addAreaVo.getProvince());
			area.setAddress(addAreaVo.getAddress());
			area.setCity(addAreaVo.getCity());
			area.setDistrict(addAreaVo.getDistrict());
			area.setPropertyNo(addAreaVo.getPropertyKey());
			area = generateAreaNo(area);
			if (checkArea(area.getAreaName(), area.getPropertyNo(), null))
				return Result.error("已经存在!");
			area = tFAreaService.save(area);
			if (area.getAreaId() > 0)
				return Result.success("添加成功!");
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("添加失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	/**
	 * 修改查询
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "修改查询设备区域信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "修改查询设备区域信息")
	@RequestMapping(value = "/searchAreaInfo", produces = "application/json;charset=UTF-8")
	public Result searchAreaInfo(@RequestParam("id") Integer id) {
		try {
			long start = System.currentTimeMillis();
			TFArea area = tFAreaService.getById(id);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			if (EmptyUtils.isNotEmpty(area))
				return Result.success("查询成功").withData(area);
			return Result.error("编辑失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	@ApiOperation(value = "修改设备区域信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//(value = "修改设备区域信息")
	@RequestMapping(value = "/updateAreaInfo", produces = "application/json;charset=UTF-8")
	// @ResponseStatus(HttpStatus.BAD_REQUEST)		
	public Result updateAreaInfo(@RequestParam("json") String updateAreaVoParam,HttpServletRequest request) {// MethodArgumentNotValidException方法参数NotValid
		try {	
			String usertoken=request.getHeader("usertoken");
			if(EmptyUtils.isEmpty(usertoken))
				return Result.error("还未登陆!");
			TFOperator currentUser=JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if(EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			UpdateAreaVo updateAreaVo = JSON.parseObject(updateAreaVoParam, UpdateAreaVo.class);
			TFArea tfArea = new TFArea();
			tfArea.setAreaId(updateAreaVo.getAreaId());
			tfArea.setAreaName(updateAreaVo.getAreaName());
			tfArea.setAttributes(updateAreaVo.getAttributes());
			tfArea.setProvince(updateAreaVo.getProvince());
			tfArea.setDistrict(updateAreaVo.getDistrict());
			tfArea.setCity(updateAreaVo.getCity());
			tfArea.setAddress(updateAreaVo.getAddress());
			tfArea.setUpdateBy(String.valueOf(currentUser.getOperatorId()));
			tfArea.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			long start = System.currentTimeMillis();
			TFArea checkArea = tFAreaService.getById(updateAreaVo.getAreaId());
			if (checkArea(tfArea.getAreaName(), checkArea.getSecretKey(), updateAreaVo.getAreaId()))
				return Result.error("已经存在,不能重复！");
			TFArea area = tFAreaService.updateById(tfArea);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			if (EmptyUtils.isNotEmpty(area))
				return Result.success("编辑成功").withData(area);
			return Result.error("编辑失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	public boolean checkArea(String areaName, String key, Integer areaId) {
		TFArea area = new TFArea();
		boolean iftrue = false;
		area.setSecretKey(key);
		area.setAreaName(areaName);
		area = tFAreaService.getByCondition(area);
		iftrue = (EmptyUtils.isNotEmpty(area))
				? ((areaId != 0 || areaId != null) ? (iftrue = area.getAreaId() == areaId ? false : true) : true)
				: false;
		return iftrue;
	}

	/**
	 * 生产区域编号
	 * 
	 * @param tfArea
	 * @return
	 */
	private TFArea generateAreaNo(TFArea tfArea) {
		Random random = new Random();
		boolean ifHave = true;
		String areaNo = String.valueOf(random.nextInt(10000));
		do {
			TFArea area = new TFArea();
			area.setAreaNo(areaNo);
			TFArea checkRoleno = tFAreaService.getByCondition(area);
			if (EmptyUtils.isEmpty(checkRoleno)) {
				tfArea.setAreaNo(areaNo);
				ifHave = false;
			} else {
				areaNo = String.valueOf(random.nextInt(10000));
				ifHave = true;
			}
		} while (ifHave);
		return tfArea;
	}

	public Result checkAreaUnderDevice(String[] ids) {
		List<TFArea> tfAreas = tFAreaService.findCheckAreaList(ids);
		for (TFArea tfArea : tfAreas) {
			if (tfArea.getDevices().size() >= 1) {
				return Result.error(tfArea.getAreaName() + "该区域下绑定了" + tfArea.getDevices().size() + "个设备!");
			}
		}
		return Result.success();
	}
}
