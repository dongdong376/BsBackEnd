package com.jca.systemset.controller.role;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFRole;
import com.jca.databeans.pojo.TFRoleMenuResource;
import com.jca.databeans.vo.AddRole;
import com.jca.databeans.vo.UpdateRole;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;
import com.jca.systemset.service.operator.TFOperatorService;
import com.jca.systemset.service.resource.TFRoleMenuResourceService;
import com.jca.systemset.service.role.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("角色API")
@RestController
@RequestMapping("/rolemanage/role")
public class RoleController {
	@Resource
	private RoleService roleService;
	@Resource
	private TFRoleMenuResourceService tFRoleMenuResourceService;
	@Resource
	private TFOperatorService tFOperatorService;
	@Resource
	private RedisAPI redisAPI;

	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询所有角色信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查找所有角色信息")
	@RequestMapping(value = "/findAllRole", produces = "application/json;charset=UTF-8")
	public Result findAllRole() {
		long start = System.currentTimeMillis();
		List<TFRole> tfRoles = roleService.listAll();
		for (TFRole tfRole : tfRoles) {
			if (tfRole.getCreateTime() == null || tfRole.getCreateTime().equals(""))
				tfRole.setCreateTime("--");
			if (tfRole.getUpdateTime() == null || tfRole.getUpdateTime().equals(""))
				tfRole.setUpdateTime("--");
		}
		long end = System.currentTimeMillis();
		log.info("所耗时间==>" + (end - start));
		return Result.success().withData(tfRoles);
	}

	/**
	 * 删除角色
	 * 
	 * @param roleIds
	 * @return
	 */
	@ApiOperation(value = "删除角色信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "删除角色信息")
	@RequestMapping(value = "/removeRole", produces = "application/json;charset=UTF-8")
	public Result removeRole(@RequestParam(value = "ids") String roleIds) {
		String roles[] = roleIds.split(",");
		try {
			long start = System.currentTimeMillis();
			boolean ifsuccess = false;
			// 查询角色
			List<TFRole> tfRoles = roleService.findRoleList(roles);
			for (TFRole tfRole : tfRoles) {
				if (tfRole.getOperatorSum() >= 1) {
					return Result.error(tfRole.getRoleName() + "下有" + tfRole.getOperatorSum() + "个账户,不能删除!");
				} else {
					// 删除角色
					ifsuccess = roleService.deleteById(tfRole.getRoleId());
					// 删除角色关联的资源
					tFRoleMenuResourceService
							.deleteByCondition(TFRoleMenuResource.builder().roleNo(tfRole.getRoleNo()).build());
				}
			}
			// Integer delResult = 0; // roleService.deleteRoleInfo(roles);
			long end = System.currentTimeMillis();
			log.info("所耗时间==>" + (end - start));
			if (ifsuccess) {
				return Result.success("删除成功");
			} else {
				return Result.success("删除失败");
			}
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.success("操作失败");
		}

	}

	/**
	 * 添加角色
	 * 
	 * @param AddRoleJsonParam
	 * @return
	 */
	@ApiOperation(value = "添加角色信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "添加角色信息")
	@RequestMapping(value = "/AddRole", produces = "application/json;charset=UTF-8")
	public Result AddRole(@RequestParam(value = "json") String AddRoleJsonParam, HttpServletRequest request,
			@RequestParam(required = false) String token) {		
		String usertoken = request.getHeader("usertoken");
//		if (EmptyUtils.isEmpty(usertoken))
//			return Result.error("你还未登陆");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(token), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆");
		// 添加的json参数
		// 转为实体对象
		AddRole addrole = JSON.parseObject(AddRoleJsonParam, AddRole.class);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		TFRole role = new TFRole();
		// 生成角色编号
		role.setRoleNo(UUIDUtils.generateUUID(5));
		role.setRoleName(addrole.getRoleName());
		role.setRemarks(addrole.getRemarks());
		role.setCreateBy(currentUser.getOperatorId());
		role.setIsDown(0);// 默认禁用
		role.setCreateTime(format.format(new Date()));
		if (this.checkRoleName(role.getRoleName(), null))
			return Result.exist("角色已经存在!");
		TFRole resultRole = roleService.save(role);
		if (EmptyUtils.isNotEmpty(resultRole)) {
			for (Integer menuId : addrole.getMenuId()) {
				TFRoleMenuResource menuResource = tFRoleMenuResourceService.getOneInfo(resultRole.getRoleNo(), menuId);
				if (EmptyUtils.isEmpty(menuResource)) {
					// 添加角色与资源的关联
					menuResource = new TFRoleMenuResource();
					menuResource.setRoleNo(resultRole.getRoleNo());
					menuResource.setMenuId(menuId);
					menuResource.setCreateBy(1);
					menuResource.setCreateTime(format.format(new Date()));
					menuResource = tFRoleMenuResourceService.save(menuResource);
				}
			}
			return Result.success("添加成功");
		} else {
			return Result.error("添加失败");
		}
	}

	/**
	 * 修改角色
	 * 
	 * @param updateRoleJsonParam
	 * @return
	 */
	@ApiOperation(value = "修改角色信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "修改角色信息")
	@RequestMapping(value = "/UpdateRole", produces = "application/json;charset=UTF-8")
	public Result UpdateRole(@RequestParam(value = "json") String updateRoleJsonParam, HttpServletRequest request) {
		String usertoken = request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("无token");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆");
		// 添加的json参数
		UpdateRole updateRole = JSON.parseObject(updateRoleJsonParam, UpdateRole.class);
		try {
			long start = System.currentTimeMillis();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			TFRole role = new TFRole();
			role.setRoleName(updateRole.getRoleName());
			role.setRemarks(updateRole.getRemarks());
			role.setRoleId(updateRole.getRoleId());// 主键
			role.setUpdateBy(currentUser.getOperatorId());
			role.setIsDown(0);// 默认禁用
			role.setUpdateTime(format.format(new Date()));
			// 进行唯一性验证
			if (this.checkRoleName(role.getRoleName(), role.getRoleId()))
				return Result.exist("角色已经存在!");
			// 进行修改
			TFRole resultRole = roleService.updateRoleInfo(role);
			// 进行关联资源
			if (EmptyUtils.isNotEmpty(resultRole)) {
				for (Integer menuId : updateRole.getMenuId()) {
					TFRoleMenuResource menuResource = tFRoleMenuResourceService.getOneInfo(resultRole.getRoleNo(),
							menuId);
					if (EmptyUtils.isEmpty(menuResource)) {
						// 添加角色与资源的关联
						menuResource = new TFRoleMenuResource();
						menuResource.setRoleNo(resultRole.getRoleNo());
						menuResource.setMenuId(menuId);
						menuResource.setCreateBy(currentUser.getOperatorId());
						menuResource.setCreateTime(format.format(new Date()));
						menuResource = tFRoleMenuResourceService.save(menuResource);
					}
				}
				long end = System.currentTimeMillis();
				log.info("耗时==>" + (end - start));
				return Result.success("修改成功");
			} else {
				return Result.error("修改失败");
			}
		} catch (Exception e) {
			log.error("异常==>" + e.getMessage());
			return Result.error("存在异常，请终止操作!");
		}
	}

	/**
	 * 获取修改的信息
	 * 
	 * @param roleId
	 * @return
	 */
	@ApiOperation(value = "获取角色信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "获取角色信息")
	@RequestMapping(value = "/getUpdateInfo", produces = "application/json;charset=UTF-8")
	public Result getUpdateInfo(@RequestParam(value = "roleId") Integer roleId) {
		long start = System.currentTimeMillis();
		// 查询角色
		TFRole role = roleService.getById(roleId);
		// 查询资源
		List<TFRoleMenuResource> resources = tFRoleMenuResourceService
				.listByCondition(TFRoleMenuResource.builder().roleNo(role.getRoleNo()).build());
		Integer menuIds[] = new Integer[resources.size()];
		role.setMenuId(menuIds);
		for (int i = 0; i < resources.size(); i++) {
			role.getMenuId()[i] = resources.get(i).getMenuId();
		}
		long end = System.currentTimeMillis();
		log.info("耗时==>" + (end - start));
		return Result.success().withData(role);
	}

	/**
	 * 唯一验证
	 * 
	 * @param roleName需验证参数
	 * @param roleId需修改的主键id
	 * @return
	 */
	private boolean checkRoleName(String roleName, Integer roleId) {
		TFRole role = roleService.getByCondition(TFRole.builder().roleName(roleName).build());
		if (EmptyUtils.isEmpty(role)) {// 没有相同直接修改
			return false;
		} else {
			if (EmptyUtils.isNotEmpty(roleId)) {
				if (roleId != role.getRoleId()) {// 不是同一个角色不能修改为一个名称
					return true;
				} else {
					return false;// 为同一角色可以修改为一样
				}
			} else {
				return true;
			}
		}
	}

	/**
	 * 生产角色编号
	 * 
	 * @param role
	 * @return
	 */
	private TFRole generateRoleNo(TFRole role) {
		Random random = new Random();
		boolean ifHave = true;
		String roleNo = String.valueOf(random.nextInt(10000));
		do {
			TFRole checkRoleno = roleService.getByCondition(TFRole.builder().roleNo(roleNo).build());
			if (EmptyUtils.isEmpty(checkRoleno)) {
				role.setRoleNo(roleNo);
				ifHave = false;
			} else {
				roleNo = String.valueOf(random.nextInt(10000));
				ifHave = true;
			}
		} while (ifHave);
		return role;
	}
}
