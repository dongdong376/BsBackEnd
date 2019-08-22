package com.jca.systemset.controller.property;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFProperty;
import com.jca.databeans.vo.AddPropertyVo;
import com.jca.databeans.vo.UpdatePropertyVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.ExcelUtil;
import com.jca.datacommon.tool.Result;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;
import com.jca.systemset.service.property.PropertyService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/propertymanage/property")
public class PropertyController {
	@Resource
	private PropertyService propertyService;
	@Resource
	private RedisAPI redisAPI;

	@ApiOperation(value = "将所有物业分组统计", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "将所有物业分组统计")
	@RequestMapping(value = "/countAllProperty", produces = "application/json;charset=UTF-8")
	public Result countAllProperty() {
		Long start = System.currentTimeMillis();
		List<TFProperty> properties = propertyService.listAll();
		properties.stream().forEach(p -> {
			String addr = p.getAddress();
			p.setAddress(p.getProvince() + "省" + p.getCity() + p.getDistrict() + addr);
			if (p.getAddress() == null)
				p.setAddress("----");
		});

		Map<String, List<TFProperty>> map = new HashMap<>();
		for (TFProperty property : properties) {
			if (map.containsKey(property.getCity())) {// map中存在此id，将数据存放当前key的map中
				map.get(property.getCity()).add(property);
			} else {// map中不存在，新建key，用来存放数据
				List<TFProperty> tmpList = new ArrayList<>();
				tmpList.add(property);
				map.put(property.getCity(), tmpList);
			}
		}
		Long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success().withData(map);
	}

	@ApiOperation(value = "查询所有物业信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查找物业信息")
	@RequestMapping(value = "/findAllProperty", produces = "application/json;charset=UTF-8")
	public Result findAll() {
		Long start = System.currentTimeMillis();
		List<TFProperty> properties = propertyService.listAll();
		properties.stream().forEach(p -> {
			String addr = p.getAddress();
			p.setAddress(p.getProvince() + "省" + p.getCity() + p.getDistrict() + addr);
			if (p.getAddress() == null)
				p.setAddress("----");
		});
		Long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success().withData(properties);
	}

	@ApiOperation(value = "删除物业信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "删除物业信息")
	@RequestMapping(value = "/removeProperty", produces = "application/json;charset=UTF-8")
	public Result removeProperty(@RequestParam(value = "ids") String proIds) {
		try {
			String ids[] = proIds.split(",");
			Result checkedResult=propertyService.propertyDevice(ids);
			if(checkedResult.getCode()!=200)
				return checkedResult;
			Integer result = propertyService.removePrpoerty(ids);
			if (result > 0)
				return Result.success("删除成功!");
			return Result.error("删除失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!").withData(e.getMessage());
		}
	}

	@ApiOperation(value = "物业注册", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "物业注册")
	@RequestMapping(value = "/PropertyRegister", produces = "application/json;charset=UTF-8")
	public Result PropertyRegister(@RequestParam("json") String propertyVoParam,HttpServletRequest request) {
		try {
			String usertoken=request.getHeader("usertoken");
			if(EmptyUtils.isEmpty(usertoken))
				return Result.error("还未登陆!");
			TFOperator currentUser=JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if(EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			AddPropertyVo propertyVo = JSON.parseObject(propertyVoParam, AddPropertyVo.class);
			if (EmptyUtils.isEmpty(propertyVo.getPropertyName()) || EmptyUtils.isEmpty(propertyVo.getContacts())
					|| EmptyUtils.isEmpty(propertyVo.getTelephone()) || EmptyUtils.isEmpty(propertyVo.getAddress()))
				return Result.error("信息不完整,星号项为必填项!");
			Long start = System.currentTimeMillis();
			// 创建添加实体
			TFProperty property = new TFProperty();
			BeanUtils.copyProperties(propertyVo, property);
			property.setCreateBy(currentUser.getOperatorId());
			property.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if (this.checkPropertyName(property.getPropertyName(), null))
				return Result.error("" + property.getPropertyName() + "物业已经被注册过");
			property.setPropertyNo(UUIDUtils.generateUUID(6));
			TFProperty result = propertyService.save(property);
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			if (EmptyUtils.isNotEmpty(result.getPropertyId()))
				return Result.success("添加成功!").withData(result);
			return Result.error("添加失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!").withData(e);
		}
	}

	@ApiOperation(value = "物业查询", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "物业查询")
	@RequestMapping(value = "/getPropertyInfo", produces = "application/json;charset=UTF-8")
	public Result getPropertyInfo(@RequestParam(value = "id") Integer propertyId) {
		try {
			Long start = System.currentTimeMillis();
			TFProperty property = propertyService.getById(propertyId);
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success().withData(property);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("存在异常!").withData(e);
		}
	}

	@ApiOperation(value = "物业修改", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "物业修改")
	@RequestMapping(value = "/updatePropertyInfo", produces = "application/json;charset=UTF-8")
	public Result updatePropertyInfo(@RequestParam(value = "json") String propertyVoParam,HttpServletRequest request) {
		try {
			String usertoken=request.getHeader("usertoken");
			if(EmptyUtils.isEmpty(usertoken))
				return Result.error("还未登陆!");
			TFOperator currentUser=JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if(EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			UpdatePropertyVo propertyVo = JSON.parseObject(propertyVoParam, UpdatePropertyVo.class);
			Long start = System.currentTimeMillis();
			if (EmptyUtils.isEmpty(propertyVo.getPropertyName()) || EmptyUtils.isEmpty(propertyVo.getContacts())
					|| EmptyUtils.isEmpty(propertyVo.getTelephone()) || EmptyUtils.isEmpty(propertyVo.getAddress()))
				return Result.error("信息不完整,星号项为必填项!");
			TFProperty property = new TFProperty();
			BeanUtils.copyProperties(propertyVo, property);
			property.setUpdateBy(currentUser.getOperatorId());
			property.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if (checkPropertyName(propertyVo.getPropertyName(), propertyVo.getPropertyId()))
				return Result.error("物业名已经存在!");
			TFProperty updateProperty = propertyService.updatePropertyInfo(property);
			if (EmptyUtils.isEmpty(updateProperty))
				return Result.error("修改失败!");
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success().withData(updateProperty);
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!").withData(e);
		}
	}

	@ApiOperation(value = "查询登录用户所在组的所有物业", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查询登录用户所在组的所有物业")
	@RequestMapping("/findLoginProperty")
	public Result findLoginProperty(@RequestParam(value = "json", required = false) String propertyGroupNo)
			throws Exception {
		Long start = System.currentTimeMillis();
		List<TFProperty> properties = propertyService
				.listByCondition(TFProperty.builder().propertyGroupNo(propertyGroupNo).build());
		CacheUtil.put("properties", properties);
		Long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success("查询成功").withData(properties);
	}

	@ApiOperation(value = "物业导出", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "物业导出")
	@RequestMapping("/propertyExport")
	public Result export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 导出参数
		// ExportExcelUtil.toExcel(propertyService.listAll(), response);
		return Result.success("导出成功");
	}

	/**
	 * 发送相应流
	 * 
	 * @param response
	 * @param fileName
	 */
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			// response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			// response.addHeader("Content-Length", "" + file.length());
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 唯一性验证
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean checkPropertyName(String name, Integer id) {
		TFProperty property = new TFProperty();
		property.setPropertyName(name);
		TFProperty checkPropertyName = propertyService.getByCondition(property);
		if (EmptyUtils.isNotEmpty(checkPropertyName)) {// 存在
			if (EmptyUtils.isNotEmpty(id)) {// 为修改时
				if (checkPropertyName.getPropertyId() == id) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 生产物业编号
	 * 
	 * @param role
	 * @return
	 */
	private String generatePropertyNo() {
		Random random = new Random();
		boolean ifHave = true;
		String propertyNo = String.valueOf(random.nextInt(10000));
		do {
			TFProperty property = new TFProperty();
			property.setPropertyNo(propertyNo);
			TFProperty checkRoleno = propertyService.getByCondition(property);
			if (EmptyUtils.isEmpty(checkRoleno)) {
				ifHave = false;
			} else {
				propertyNo = String.valueOf(random.nextInt(10000));
				ifHave = true;
			}
		} while (ifHave);
		return propertyNo;
	}

	@RequestMapping("/export")
	@ResponseBody
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取数据
		List<TFProperty> list = propertyService.listAll();// 查询到了所有的物业

		// excel标题
		String[] title = { "物业No", "物业名称", "秘钥", "创建日期", "状态" };

		// excel文件名
		String fileName = System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "物业信息表";

		String content[][] = new String[list.size()][title.length];
		for (int i = 0; i < list.size(); i++) {

			TFProperty obj = list.get(i);
			content[i][0] = obj.getPropertyNo().toString();
			content[i][1] = obj.getPropertyName();
			content[i][2] = obj.getSecretKey();
			content[i][3] = obj.getCreateTime();
			content[i][4] = obj.getIsDown().toString();
		}

		// 创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
		// 响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
