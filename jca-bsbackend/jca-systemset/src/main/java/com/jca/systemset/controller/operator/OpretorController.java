package com.jca.systemset.controller.operator;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.databeans.pojo.TFRole;
import com.jca.databeans.pojo.TFRoleMenuResource;
import com.jca.databeans.vo.AddOperatorVo;
import com.jca.databeans.vo.UpdateOperatorVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.systemset.service.operator.TFOperatorService;
import com.jca.systemset.service.property.group.PropertyGroupService;
import com.jca.systemset.service.resource.TFRoleMenuResourceService;
import com.jca.systemset.service.role.RoleService;
import com.jca.systemset.task.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 账号管理控制类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
@Slf4j
@Api(value = "账号管理")
//@MethodLog(value = "账号管理")
@RestController
@RequestMapping("/sysmanage/opretor")
public class OpretorController {

	@Resource
	private TFOperatorService tFOperatorService;
	@Resource
	private RedisAPI redisAPI;
	@Resource
	private RoleService roleService;
	@Resource
	private PropertyGroupService propertyGroupService;
	@Resource
	private TFRoleMenuResourceService TFRoleMenuResourceService;

	/**
	 * 查询账号所有信息
	 * 
	 * @param pageForm
	 * @param operatorVo
	 * @return
	 */
	@ApiOperation(value = "查询所有账号信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查找当前登录用户下所有客户账号信息")
	@RequestMapping(value = "/findAllOperator", produces = "application/json;charset=UTF-8")
	public Result findOperator(HttpServletRequest request) {
		long start = System.currentTimeMillis();
		List<TFOperator> operatorVos = tFOperatorService.listAll("operator_id asc");
		// 查询所有角色
		List<TFRole> tfRoles = roleService.listAll(null);
		// 属性组列表
		List<TFPropertyGroup> tfPropertyGroups = propertyGroupService.listAll();
		for (TFOperator tfOperator : operatorVos) {
			for (TFRole role : tfRoles) {
				if (tfOperator.getRoleNo().equals(role.getRoleNo())) {
					tfOperator.setRoleName(role.getRoleName());
				}
			}
			tfOperator.setCreateTime(
					EmptyUtils.isNotEmpty(tfOperator.getCreateTime()) ? tfOperator.getCreateTime().toString() : "--");
			tfOperator.setUpdateTime(
					EmptyUtils.isNotEmpty(tfOperator.getUpdateTime()) ? tfOperator.getUpdateTime().toString() : "--");
			tfOperator.setIsDown(Integer.valueOf(tfOperator.getIsDown()) == 1 ? "启用" : "停用");
		}
		for (TFOperator tfOperator : operatorVos) {
			for (TFPropertyGroup tfPropertyGroup : tfPropertyGroups) {
				String[] group = tfOperator.getPropertyGroupNo().split(",");
				for (String string : group) {
					if (string.equals(tfPropertyGroup.getPropertyGroupNo())) {
						tfOperator.setGroupName(tfPropertyGroup.getPropertyGroupName());
					}
				}
			}
		}
		long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success("查询成功").withData(operatorVos);
	}

	/**
	 * 编辑账号信息
	 * 
	 * @param operatorVo
	 * @return
	 */
	@ApiOperation(value = "修改账号信息", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "修改客户账号信息")
	@RequestMapping(value = "/updateOperatorInfo", produces = "application/json;charset=UTF-8")
	public Result updateOperatorInfo(@RequestParam(required = true, value = "json") String UpdateOperatorVo,
			HttpServletRequest request) {
		try {
			String usertoken = request.getHeader("usertoken");
			if (EmptyUtils.isEmpty(usertoken))
				return Result.error("无token");
			TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if (EmptyUtils.isEmpty(currentUser))
				return Result.error("你还未登陆");
			long start = System.currentTimeMillis();
			// 强转为对应实体
			UpdateOperatorVo operatorVo = JSON.parseObject(UpdateOperatorVo, UpdateOperatorVo.class);
			TFOperator tfOperator = new TFOperator();
			tfOperator.setUpdateBy(currentUser.getOperatorId());
			tfOperator.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 菜单编号
			Integer menuId[] = operatorVo.getMenuId();
			// 值交换
			BeanUtils.copyProperties(operatorVo, tfOperator);			
			//唯一验证
			if(!StringUtils.isEmpty(tfOperator.getOperatorNo()))
			if (this.checkOperator(tfOperator.getOperatorNo(), tfOperator.getOperatorId()))
				return Result.exist("已经存在该账号!");
			// 修改账号信息
			tfOperator = tFOperatorService.updateById(tfOperator);
			if (EmptyUtils.isNotEmpty(tfOperator)) {
				if (EmptyUtils.isEmpty(tfOperator.getRoleNo()))
					return Result.error("没有与角色关联!");
				// 账号所属角色
				TFRole role = roleService.getSignleRole(tfOperator.getRoleNo());
				if (EmptyUtils.isEmpty(role))
					return Result.error("此账户没有和角色关联,暂无权限!");
				// 资源操作，插入资源表
				for (Integer integer : menuId) {// 菜单数组
					// 查找角色和菜单关联的中间表
					TFRoleMenuResource resource = TFRoleMenuResourceService.getOneInfo(role.getRoleNo(), integer);
					log.info("开始操作==>");
					if (EmptyUtils.isEmpty(resource)) {// 不存在为新关联进行添加操作
						resource = new TFRoleMenuResource();
						resource.setCreateBy(currentUser.getOperatorId());
						resource.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						resource.setMenuId(integer);
						resource.setRoleNo(role.getRoleNo());
						resource = TFRoleMenuResourceService.save(resource);
						log.info("不存在此关联资源插入结束==>");
					} else {// 进行添加
						continue;
					}
				}
				long end = System.currentTimeMillis();
				log.info("耗时==>" + (end - start));
				return Result.success("修改成功!");
			}			
			return Result.success("修改失败!");
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("异常");
		}
	}

	/**
	 * 添加账号信息
	 * 
	 * @param operatorVo
	 * @return
	 */
	@ApiOperation(value = "添加账号信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "添加账号信息")
	@RequestMapping(value = "/addOperatorInfo", produces = "application/json;charset=UTF-8")
	public Result addOperatorInfo(@RequestParam(value = "json") String addOperatorVo, HttpServletRequest request) {
		String usertoken = request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("无token");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆");
		// 强转为实体
		AddOperatorVo operatorVo = JSON.parseObject(addOperatorVo, AddOperatorVo.class);
		long start = System.currentTimeMillis();
		if (EmptyUtils.isEmpty(operatorVo.getOperatorNo()) && EmptyUtils.isEmpty(operatorVo.getOperatorName())
				&& EmptyUtils.isEmpty(operatorVo.getPassword())
				&& EmptyUtils.isEmpty(operatorVo.getPropertyGroupNo())) {
			return Result.error("此信息不能为空!");
		}
		TFOperator operator = new TFOperator();
		// 添加账号信息
		BeanUtils.copyProperties(operatorVo, operator);
		operator.setCreateBy(currentUser.getOperatorId());		
		try {
			operator.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
		} catch (ParseException e1) {
			log.error("时间有误!",e1.getMessage());
			return Result.error("时间有误!");
		}
		try {
			// 物业组
			if (this.checkOperator(operator.getOperatorNo(), null))
				return Result.exist("已经存在该账号!");
			// 保存账号信息
			TFOperator opResult = tFOperatorService.save(operator);
			// 设置账户和菜单资源关联
			if (EmptyUtils.isNotEmpty(opResult)) {
				log.info("添加关联资源==>" + opResult.getOperatorId());
				for (Integer menuId : operatorVo.getMenuId()) {
					// 查找角色和菜单关联的中间表
					TFRoleMenuResource resource = TFRoleMenuResourceService.getOneInfo(opResult.getRoleNo(), menuId);
					if (EmptyUtils.isEmpty(resource)) {// 相应菜单和账号无关联资源才进行插入
						resource = new TFRoleMenuResource();
						resource.setCreateBy(currentUser.getOperatorId());
						resource.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
						resource.setMenuId(menuId);
						resource.setRoleNo(opResult.getRoleNo());
						resource = TFRoleMenuResourceService.save(resource);
					}
				}
				long end = System.currentTimeMillis();
				log.info("耗时==>" + (end - start));
				return Result.success("添加成功").withData(operator);
			} else {
				return Result.error("添加失败!");
			}
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("异常!");
		}
	}

	/**
	 * 获取账号信息
	 * 
	 * @param opId
	 * @param roleNo
	 * @param groupNo
	 * @return
	 */
	@ApiOperation(value = "查询修改账号信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查询修改账号信息")
	@RequestMapping(value = "/getOperatorInfoByid", produces = "application/json;charset=UTF-8")
	public Result getOperatorInfoByid(@RequestParam(required = true, value = "id") Integer opId) {
		long start = System.currentTimeMillis();
		Map map = new HashMap<>();
		// 使用线程调用服务
		Callable<TFOperator> taskService = new TaskService<TFOperator>() {
			@Resource
			private TFOperatorService operatorService;
			private TFOperator operator;

			@Override
			public TFOperator call() throws Exception {
				log.info("当前线程名：" + Thread.currentThread().getName());
				operator = tFOperatorService.getById(opId);
				return operator;
			}
		};
		// 账号信息
		TFOperator operator = null;
		try {
			operator = taskService.call();// tFOperatorService.getById(opId);
			if (EmptyUtils.isEmpty(operator.getRoleNo()))
				map.put("roleInfo", "没有与角色关联!");
			if (EmptyUtils.isEmpty(operator.getPropertyGroupNo()))
				map.put("groupInfo", "没有与物业组关联!");
			// 角色信息
			TFRole tfRole = roleService.getSignleRole(operator.getRoleNo());
			if (EmptyUtils.isEmpty(tfRole))
				return Result.error("此账号没有与角色关联无任何权限");
			// 角色资源列表
			List<TFRoleMenuResource> tfResources = TFRoleMenuResourceService
					.listByCondition(TFRoleMenuResource.builder().roleNo(tfRole.getRoleNo()).build());
			operator.setRoleName(tfRole.getRoleName());
			map.put("operator", operator);
			map.put("tfResources", tfResources);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end + start));
			return Result.success().withData(map);
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常");
		}
	}

	public Boolean checkOperator(String opNo, Integer operatorId) {
		TFOperator Addoperator = tFOperatorService.getByCondition(TFOperator.builder().operatorNo(opNo).build());
		if (EmptyUtils.isNotEmpty(Addoperator)) {
			if (EmptyUtils.isNotEmpty(operatorId)) {
				if (operatorId != Addoperator.getOperatorId()) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
