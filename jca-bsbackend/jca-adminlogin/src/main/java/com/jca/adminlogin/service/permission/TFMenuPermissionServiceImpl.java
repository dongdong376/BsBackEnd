package com.jca.adminlogin.service.permission;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jca.adminlogin.service.menu.MenuService;
import com.jca.databeans.pojo.RoleResource;
import com.jca.databeans.pojo.TFMenu;
import com.jca.databeans.pojo.TFMenuPermission;
import com.jca.databeans.vo.LoginSuccessVO;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.cache.UserCacheKey;
import com.jca.datacommon.enums.ResourceTypeEnum;
import com.jca.datacommon.enums.StatusEnum;
import com.jca.datacommon.exception.BusinessRuntimeException;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.PlaceholderResolver;
import com.jca.datacommon.web.UriPattern;
import com.jca.datadao.RoleResourceMapper;
import com.jca.datadao.TFMenuPermissionMapper;
import com.jca.datadao.TFPropertyMapper;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;

/**
 * 权限服务实现类
 * 
 * @author:
 * @date:
 */
@SuppressWarnings({"rawtypes","unused","unchecked"})
@Service
public class TFMenuPermissionServiceImpl extends BaseServiceImpl<TFMenuPermissionMapper, TFMenuPermission>
		implements TFMenuPermissionService{
	/**
	 * 占位符解析器
	 */
	private static PlaceholderResolver resolver = PlaceholderResolver.getDefaultResolver();

	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private TFMenuPermissionMapper permissionMapper;
	@Autowired
	private RoleResourceMapper roleResourceMapper;
	@Resource
	private TFPropertyMapper tFPropertyMapper;
	@Resource
	private RedisAPI redisAPI;
	@Autowired
	private MenuService menuService;

	/**
	 * 保存并授权
	 */
	@MethodLog("保存用户和菜单")
	@Override
	public LoginSuccessVO saveIdAndPermission(Integer id) {
		String token = UUIDUtils.generateUUID();		
		// 获取当前用户的权限列表
		List<TFMenu> menus = menuService.getByUserId(id);	
		return LoginSuccessVO.builder().menuToken(token).menus(menus).build();
	}

	@Override
	public Integer getIdByToken(String token) {
		return (Integer) redisTemplate.opsForValue().get(resolver.resolveByObject(UserCacheKey.USER_ID_KEY, token));
	}

	@Override
	public List<UriPattern> getUriPermissionByToken(String token) {
		return (List<UriPattern>) redisTemplate.opsForValue()
				.get(resolver.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, token));
	}

	@Override
	public TFMenuPermission changeStatus(Integer id, Integer status) {
		TFMenuPermission p = getById(id);		
		p.setStatus(status);
		p.setUpdateTime(LocalDateTime.now());
		updateById(p);
		return p;
	}

	@Override
	public TFMenuPermission savePermission(TFMenuPermission permission) {
		if (countByCondition(TFMenuPermission.builder().code(permission.getCode()).build()) != 0) {
			throw new BusinessRuntimeException("该权限code已经存在！");
		}
		LocalDateTime now = LocalDateTime.now();
		permission.setCreateTime(now);
		permission.setUpdateTime(now);
		permission.setStatus(StatusEnum.ENABLE.getValue());
		return save(permission);
	}

	@Override
	public TFMenuPermission updatePermission(TFMenuPermission permission) {
		TFMenuPermission old = getById(permission.getMenuPermissionId());
		if (old == null) {
			throw new BusinessRuntimeException("权限id不存在！");
		}
		// 如果旧的权限code与新权限code不同，则需校验新的code
		if (!old.getCode().equals(permission.getCode())) {
			if (countByCondition(TFMenuPermission.builder().code(permission.getCode()).build()) != 0) {
				throw new BusinessRuntimeException("该权限code已经存在！");
			}
		}
		permission.setUpdateTime(LocalDateTime.now());
		return updateById(permission);
	}

	@Transactional
	@Override
	public Boolean deletePermission(Integer id) {
		TFMenuPermission p = getById(id);
		if (p == null) {
			throw new BusinessRuntimeException("权限不存在！");
		}
		if (deleteById(id)) {
			roleResourceMapper.deleteByCriteria(
					RoleResource.builder().resourceId(id).type(ResourceTypeEnum.PERMISSION.getValue()).build());			
			return true;
		}
		return false;
	}	
}
