package com.jca.datadao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jca.databeans.pojo.TFRoleMenuResource;
import com.jca.datacommon.base.BaseSqlProvider;

public interface TFRoleMenuResourceMapper extends BaseSqlProvider<TFRoleMenuResource> {
	@Select("SELECT \r\n" + 
			"  `role_menu_id` AS roleMenuId,\r\n" + 
			"  `role_no` AS roleNo,\r\n" + 
			"  `menu_id` AS menuId,\r\n" + 
			"  `type` AS TYPE,\r\n" + 
			"  `create_by` AS createBy,\r\n" + 
			"  `create_time` AS createTime\r\n" + 
			"	FROM\r\n" + 
			"  t_f_role_menu_resource  fmr WHERE fmr.role_no=#{roleNo}  AND fmr.menu_id=#{menuId}")
	TFRoleMenuResource getSingleInfo(@Param(value="roleNo")String roleNo,@Param(value="menuId")Integer menuId);
}
