package com.jca.systemset.controller.property.group;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFProperty;
import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.databeans.pojo.TFRole;
import com.jca.databeans.vo.AddGroupVo;
import com.jca.databeans.vo.PropertyGroupVo;
import com.jca.databeans.vo.UpdateGroupVo;
import com.jca.databeans.vo.UpdateOperatorVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;
import com.jca.systemset.service.property.PropertyService;
import com.jca.systemset.service.property.group.PropertyGroupService;
import com.jca.systemset.service.property.group.PropertyGroupVoService;
import com.jca.systemset.service.role.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "账号管理")
//@MethodLog(value = "账号管理")
@RestController
@RequestMapping("/propertyGroumanage/propertyGroup")
public class PropertyGroupController {
	@Resource
	private PropertyGroupService propertyGroupService;
	@Resource
	private PropertyService propertyService;
	@Resource
	private RoleService roleService;
	@Resource
	private RedisAPI redisAPI;
	@Resource
	private PropertyGroupVoService propertyGroupVoService;

	@ApiOperation(value = "获取所有物业组信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "获取所有物业组信息")
	@RequestMapping(value = "/findAllpropertyGroup", produces = "application/json;charset=UTF-8")
	public Result findAllpropertyGroup(
			@RequestParam(required = false) @ApiParam(value = "指定orderBy排序语句") String orderBy) {
		long start = System.currentTimeMillis();
		List<PropertyGroupVo> groupVos = propertyGroupVoService.findAllPropertyGroup();
		List<TFProperty> properties = propertyService.findProperty();
		groupVos.stream().forEach(vo -> {
			StringBuilder sb = new StringBuilder();
			String temp = "";
			for (int i = 0; i < properties.size(); i++) {
				if (vo.getPropertyGroupNo().equals(properties.get(i).getPropertyGroupNo())) {
					temp += (properties.get(i).getPropertyName() + ",");
				}
			}
			if (EmptyUtils.isNotEmpty(temp)) {
				sb.append(temp.substring(0, temp.lastIndexOf(",")));
			}
			vo.setPropertyName(EmptyUtils.isEmpty(sb.toString()) ? "暂无关联物业" : sb.toString());
		});
		long end = System.currentTimeMillis();
		log.info("所耗时间==>" + (end - start));
		return Result.success().withData(groupVos);
	}

	@ApiOperation(value = "修改查询物业组信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "修改查询物业组信息")
	@RequestMapping(value = "/getpropertyGroupInfo", produces = "application/json;charset=UTF-8")
	public Result getpropertyGroupInfo(
			@RequestParam(required = false, value = "id") @ApiParam(value = "需修改的物业编号ID") String groupId) {
		long start = System.currentTimeMillis();
		if (EmptyUtils.isEmpty(groupId))
			return Result.error("无参数");
		PropertyGroupVo groupVo = propertyGroupVoService.getOneGroup(Integer.valueOf(groupId));
		List<TFProperty> searchPropertys = propertyService
				.listByCondition(TFProperty.builder().propertyGroupNo(groupVo.getPropertyGroupNo()).build());
		List<Integer> list = new ArrayList<>();
		searchPropertys.stream().forEach(s -> {
			if (s.getPropertyGroupNo().equals(groupVo.getPropertyGroupNo()))
				list.add(s.getPropertyId());
			groupVo.setPropertyNo(list);
		});
		long end = System.currentTimeMillis();
		log.info("所耗时间==>" + (end - start));
		return Result.success().withData(groupVo);
	}

	/**
	 * 修改物业相关信息
	 * 
	 * @param groupVo
	 * @return
	 */
	@ApiOperation(value = "修改物业组信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "修改物业组信息")
	@RequestMapping(value = "/updatePropertyGroupInfo", produces = "application/json;charset=UTF-8")
	public Result updatePropertyGroupInfo(@RequestParam("json") String groupVoParam, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		String usertoken = request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("还未登陆!");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆");
		UpdateGroupVo groupVo = JSON.parseObject(groupVoParam, UpdateGroupVo.class);
		TFPropertyGroup updateGroup = new TFPropertyGroup();
		BeanUtils.copyProperties(groupVo, updateGroup);		
		try {
			updateGroup.setUpdateBy(String.valueOf(currentUser.getOperatorId()));// 修改人
			updateGroup.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 唯一验证
			if (checkGroupName(groupVo.getPropertyGroupName(), updateGroup.getPropertyGroupId()))
				return Result.error("已有此物业组" + groupVo.getPropertyGroupName());
			// 修改物业组
			TFPropertyGroup group = propertyGroupService.updateGroupInfo(updateGroup);
			// 根据物业组No查出所有物业
			TFProperty property = new TFProperty();
			property.setPropertyGroupNo(group.getPropertyGroupNo());
			List<TFProperty> properties = propertyService.listByCondition(property);
			// 需要建立关联的物业
			String[] proeprtyIds = groupVo.getPropertyIds().split(",");
			// 修改物业
			for (TFProperty tfProperty : properties) {
				for (String no : proeprtyIds) {
					if (!no.equals(tfProperty.getPropertyId().toString())) {
						tfProperty.setPropertyGroupNo(null);
						tfProperty.setUpdateBy(1);// 修改人
						tfProperty.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						propertyService.updatePropertyInfo(tfProperty);// 取消已经建立关联的
					}
				}
			}
			property.setUpdateBy(currentUser.getOperatorId());// 修改人
			property.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			propertyService.updatePropertyInfo(property, proeprtyIds);// 与物业组建立关联
			long end = System.currentTimeMillis();
			log.info("所耗时间==>" + (end - start));
			if (EmptyUtils.isEmpty(group))
				return Result.success("修改失败!");
			return Result.success("修改成功！").withData(group);
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常!");
		}
	}

	/**
	 * 添加物业组信息
	 * 
	 * @param groupVoParam
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "添加物业组信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "添加物业组信息")
	@RequestMapping(value = "/AddPropertyGroupInfo", produces = "application/json;charset=UTF-8")
	public Result AddPropertyGroupInfo(@RequestParam("json") String groupVoParam, HttpServletRequest request) {
		try {
			long start = System.currentTimeMillis();
			String usertoken = request.getHeader("usertoken");
			if (EmptyUtils.isEmpty(usertoken))
				return Result.error("你还未登陆");
			TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if (EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			AddGroupVo groupVo = JSON.parseObject(groupVoParam, AddGroupVo.class);
			if (EmptyUtils.isEmpty(groupVo.getPropertyGroupName()))
				return Result.error("组名不能为空!");
			TFPropertyGroup addGroup = new TFPropertyGroup();
			BeanUtils.copyProperties(groupVo, addGroup);
			addGroup.setCreateBy(String.valueOf(1));// 修改人
			addGroup.setPropertyGroupNo(UUIDUtils.generateUUID(6));;
			addGroup.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 唯一验证
			if (checkGroupName(groupVo.getPropertyGroupName(), null))
				return Result.error("已有此物业组" + groupVo.getPropertyGroupName());
			// 修改物业组
			TFPropertyGroup group = propertyGroupService.save(addGroup);
			TFProperty property = new TFProperty();
			// 需要建立关联的物业
			String[] proeprtyIds = groupVo.getPropertyIds().split(",");
			property.setUpdateBy(1);// 修改人
			property.setPropertyGroupNo(group.getPropertyGroupNo());
			property.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			propertyService.updatePropertyInfo(property, proeprtyIds);// 与物业组建立关联
			long end = System.currentTimeMillis();
			log.info("所耗时间==>" + (end - start));
			return Result.success().withData(group);
		} catch (ParseException e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常,请联系...!");
		}
	}

	/**
	 * 删除物业组信息
	 * 
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除物业组信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "删除物业组信息")
	@RequestMapping(value = "/removePropertyGroupInfo", produces = "application/json;charset=UTF-8")
	public Result removePropertyGroupInfo(@RequestParam String ids) {
		long start = System.currentTimeMillis();
		if (propertyGroupService.removeGroupByArray(ids.split(",")))
			return Result.success("删除成功!");
		long end = System.currentTimeMillis();
		log.info("所耗时间==>" + (end - start));
		return Result.error("删除失败!");
	}

	/**
	 * 唯一性验证
	 * 
	 * @param groupName
	 * @param groupId
	 * @return
	 */
	public boolean checkGroupName(String groupName, Integer groupId) {
		TFPropertyGroup group = propertyGroupService
				.getByCondition(TFPropertyGroup.builder().propertyGroupName(groupName).build());
		if (EmptyUtils.isNotEmpty(group)) {
			if (EmptyUtils.isNotEmpty(groupId)) {
				if (group.getPropertyGroupId() == groupId) {
					return false;
				} else {
					return true;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private TFPropertyGroup generateGroupNo(TFPropertyGroup role) {
		Random random = new Random();
		boolean ifHave = true;
		String groupNo = String.valueOf(random.nextInt(10000));
		do {
			TFPropertyGroup checkGroupNo = propertyGroupService
					.getByCondition(TFPropertyGroup.builder().propertyGroupNo(groupNo).build());
			if (EmptyUtils.isEmpty(checkGroupNo)) {
				role.setPropertyGroupNo(groupNo);
				ifHave = false;
			} else {
				groupNo = String.valueOf(random.nextInt(10000));
				ifHave = true;
			}
		} while (ifHave);
		return role;
	}

}
