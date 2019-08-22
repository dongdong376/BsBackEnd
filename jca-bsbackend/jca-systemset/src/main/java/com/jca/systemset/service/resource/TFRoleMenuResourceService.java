package com.jca.systemset.service.resource;

import com.jca.databeans.pojo.TFRoleMenuResource;
import com.jca.datacommon.base.BaseService;

public interface TFRoleMenuResourceService extends BaseService<TFRoleMenuResource> {
	TFRoleMenuResource getOneInfo(String roleNo,Integer menuId);
}
