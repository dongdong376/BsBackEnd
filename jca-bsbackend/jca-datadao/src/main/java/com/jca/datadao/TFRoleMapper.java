package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.jca.databeans.pojo.TFRole;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;
import com.jca.datadao.sqlprovider.SelectSQLProvider;
import com.jca.datadao.sqlprovider.UpdateSQLProvider;

public interface TFRoleMapper extends BaseSqlProvider<TFRole> {
	@Select("SELECT role_id as roleId, role_no as roleNo, role_name as roleName, remarks , is_down as idDown, "
			+ "create_by as createBy, create_time as createTime, update_by as updateBy, update_time as updateTime FROM t_f_role fr WHERE fr.role_no=#{roleNo}")
	TFRole getOneRoleByCondition(@Param("roleNo") String roleNo);
	/**
	 * 查询所有角色
	 * @return
	 */
	@Select("SELECT role_id AS roleId,fr.role_no AS roleNo,role_name as roleName,remarks,fr.is_down AS isDown,"
			+ "(SELECT COUNT(0) FROM t_f_operator fo WHERE fo.role_no=fr.role_no) AS operatorSum,fr.create_time AS createTime,"
			+ "update_time as updateTime FROM t_f_role fr ")
	List<TFRole> findAllRole();
	
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer deleteRole(@Param(value="paramIds")String paramIds[]);
	
	@SelectProvider(type=SelectSQLProvider.class,method="selectRoleByRoleName")
	List<TFRole> searchRoleByRoleName(@Param(value="roleName")String roleName);
	
	@UpdateProvider(type=UpdateSQLProvider.class,method="updateRole")
	Integer updateRoleInfo(@Param(value="role") TFRole role);
	
	@SelectProvider(type=SelectSQLProvider.class,method="selectRoleByCondition")
	List<TFRole> findRoleByCondition(@Param(value="roleIds")String [] rolesId);
}
