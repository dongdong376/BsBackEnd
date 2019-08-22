package com.jca.systemset.service.role;

import java.util.List;

import com.jca.databeans.pojo.TFRole;
import com.jca.datacommon.base.BaseService;

public interface RoleService extends BaseService<TFRole> {
	TFRole getSignleRole(String roleNo);
	/**
	 * 删除多个或单个
	 * @param roleId
	 * @return
	 */
	Integer deleteRoleInfo(String roleId[]);
	
	List<TFRole> searchRoleByCondition(String roleName);
	
	TFRole updateRoleInfo(TFRole role);
	
	List<TFRole> findRoleList(String roleId[]);

}
