package com.jca.peoplemanage.controller.department;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFDepartment;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.vo.AddDepartmentVo;
import com.jca.databeans.vo.UpDepartmentVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.CollectionUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.BeanUtils;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.MD5;
import com.jca.datatool.UUIDUtils;
import com.jca.datatool.ValidationToken;
import com.jca.peoplemanage.service.department.TFDepartmentService;
import com.jca.peoplemanage.service.people.TFEmployInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Api("部门信息")
@Slf4j
@RestController
@RequestMapping("department/Info")
public class TFDepartmentController {

	@Resource
	private TFDepartmentService tFDepartmentService;
	@Resource
	private TFEmployInfoService tFEmployInfoService;
	@Resource
	private ValidationToken validationToken;

	/**
	 * 所有部门信息
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "当前物业下所有机构信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/AllDepInfo", produces = "application/json;charset=UTF-8")
	public Result AllDepInfo(HttpServletRequest request, @RequestParam(required = false) String propertyVo) {
		Long start = System.currentTimeMillis();
		if (!StringUtils.isEmpty(request.getParameter("propertyNo")))
			propertyVo = request.getParameter("propertyNo");
		List<TFDepartment> departments = tFDepartmentService
				.listByCondition(TFDepartment.builder().propertyNo(propertyVo).build());
		List<TFEmployInfo> infos = tFEmployInfoService.listAll(" employ_id desc");
		if (EmptyUtils.isEmpty(departments))
			return Result.error("暂无数据!");
		int i = 1;
		try {
			for (TFDepartment d : departments) {
				List<TFEmployInfo> emplist = new ArrayList<>();
				for (Iterator<TFEmployInfo> ite = infos.iterator(); ite.hasNext();) {
					TFEmployInfo e = (TFEmployInfo) ite.next();
					if (d.getDepartmentNo().equals(e.getDepartmentNo())) {
						emplist.add(e);
						ite.remove();
					}
				}
				d.setMemberNum(emplist.size());
				d.setIndex(i++);
				d.setInfos(emplist);
			}
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success().withData(departments).withJsonData(departments.size());
		} catch (Exception e) {
			return Result.error("改操作有误!");
		}
	}

	public List<TFDepartment> generatorTree(List<TFDepartment> lists) {
		List<TFDepartment> parentList = new ArrayList<>();
		for (Iterator<TFDepartment> ite = lists.iterator(); ite.hasNext();) {
			TFDepartment list = (TFDepartment) ite.next();
			if (StringUtils.isEmpty(list.getParentNo())) {
				parentList.add(list);
				ite.remove();
			}
		}
		// 设置子集
		parentList.stream().forEach(d1 -> {
			setChildrenTree(d1, lists);
		});
		return parentList;
	}

	public void setChildrenTree(TFDepartment parent, List<TFDepartment> lists) {
		List<TFDepartment> childrenList = new ArrayList<>();
		for (Iterator<TFDepartment> ite = lists.iterator(); ite.hasNext();) {
			TFDepartment d = (TFDepartment) ite.next();
			if (d.getDepartmentNo().equals(parent.getDepartmentNo())) {
				childrenList.add(d);
				ite.remove();
			}
		}
		if (CollectionUtils.isEmpty(childrenList))
			return;
		parent.setDeps(childrenList);
		childrenList.stream().forEach(c -> {
			setChildrenTree(c, lists);
		});
	}

	/**
	 * 添加
	 * @param request
	 * @param departmentVoJson
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "添加机构部门信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/addDepInfo", produces = "application/json;charset=UTF-8")
	public Result addDepInfo(HttpServletRequest request, @RequestParam("json") String departmentVoJson,
			@RequestParam(required = false) String token) {
		String usertoken = StringUtils.isEmpty(request.getHeader("usertoken")) ? token : request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("无token!");
		TFOperator operator = validationToken.getCurrentUser(usertoken);
		if (EmptyUtils.isEmpty(operator))
			return Result.error("未登陆!");
		try {
			AddDepartmentVo departmentVo = JSON.parseObject(departmentVoJson, AddDepartmentVo.class);
			TFDepartment e = new TFDepartment();
			e = BeanUtils.copyProperties(departmentVo, TFDepartment.class);
			// 是否为高级
			e.setParentNo(EmptyUtils.isEmpty(departmentVo.getParentNo()) ? "0" : departmentVo.getParentNo());
			e.setCreateBy(operator.getOperatorName());
			e.setIsDown(1);
			e.setPropertyNo(request.getParameter("propertyNo"));
			e.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if (EmptyUtils.isNotEmpty(tFDepartmentService.getByCondition(
					TFDepartment.builder().departmentName(e.getDepartmentName()).parentNo(e.getParentNo()).build())))
				return Result.error("存在此" + e.getDepartmentName() + "部门机构!");
			TFDepartment result = tFDepartmentService.save(e);
			if (EmptyUtils.isEmpty(result))
				return Result.error("添加失败!");
			return Result.success("添加成功!").withData(result);
		} catch (ParseException e1) {
			log.error("异常" + e1.getMessage());
			return Result.error();
		}
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param departmentVoJson
	 * @return
	 */
	@ApiOperation(value = "修改机构部门信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/upDepInfo", produces = "application/json;charset=UTF-8")
	public Result upDepInfo(HttpServletRequest request, @RequestParam("json") String departmentVoJson) {
		Long start = System.currentTimeMillis();
		String usertoken = request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("无token!");
		TFOperator operator = validationToken.getCurrentUser(usertoken);
		if (EmptyUtils.isEmpty(operator))
			return Result.error("未登陆!");
		try {
			UpDepartmentVo departmentVo = JSON.parseObject(departmentVoJson, UpDepartmentVo.class);
			TFDepartment e = new TFDepartment();
			e = BeanUtils.copyProperties(departmentVo, TFDepartment.class);
			// 是否为高
			e.setUpdateBy(operator.getOperatorId().toString());
			e.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if (checkUnique(e.getDepartmentName(), e.getParentNo(), e.getDepartmentId()))
				return Result.error("存在此" + e.getDepartmentName() + "部门机构!");
			TFDepartment result = tFDepartmentService.updateById(e);
			if (EmptyUtils.isEmpty(result))
				return Result.error("修改失败!");
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("修改成功!").withData(result);
		} catch (ParseException e1) {
			log.error("异常" + e1.getMessage());
			return Result.error();
		}
	}

	/**
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "查看机构部门信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/getDepInfo", produces = "application/json;charset=UTF-8")
	public Result getDepInfo(HttpServletRequest request, @RequestParam("id") Integer id) {
		Long start = System.currentTimeMillis();
		TFDepartment department = tFDepartmentService.getById(id);
		Long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success("查询成功!").withData(department);
	}

	@ApiOperation(value = "删除机构部门信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/removeDepInfo", produces = "application/json;charset=UTF-8")
	public Result removeDepInfo(HttpServletRequest request, @RequestParam("ids") String idsParam) {
		Long start = System.currentTimeMillis();
		String[] ids = idsParam.split(",");
		try {
			Result result = tFDepartmentService.removeDepInfo(ids);
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败!");
		}

	}

	public String generatorDepNo() {
		String depNo = String.valueOf(MD5.getRandomCode());
		if (EmptyUtils
				.isNotEmpty(tFDepartmentService.getByCondition(TFDepartment.builder().departmentNo(depNo).build()))) {
			return depNo;
		} else {
			return depNo = String.valueOf(MD5.getRandomCode());
		}
	}

	public boolean checkUnique(String depName, String parentNo, Integer id) {
		TFDepartment d = tFDepartmentService
				.getByCondition(TFDepartment.builder().departmentName(depName).parentNo(parentNo).build());
		if (EmptyUtils.isNotEmpty(d)) {
			if (d.getDepartmentId() == id) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
