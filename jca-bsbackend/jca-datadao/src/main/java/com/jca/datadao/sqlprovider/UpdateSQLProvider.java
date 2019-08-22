package com.jca.datadao.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFProperty;
import com.jca.databeans.pojo.TFRole;
import com.jca.datatool.EmptyUtils;

/**
 * 有关修改
 * @author Administrator
 *
 */
public class UpdateSQLProvider {
	
	/**
	 * 修改账户
	 * @param tfOperator
	 * @return
	 */
	public String updateOperator(@Param("tfOperator") TFOperator tfOperator) {
		SQL sql = new SQL();// jdbcType为数据库字段类型
		sql.UPDATE("t_f_operator");
		if (EmptyUtils.isNotEmpty(tfOperator.getOperatorName())) {
			sql.SET("operator_name = #{tfOperator.operatorName}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getOperatorNo())) {
			sql.SET("operator_no = #{tfOperator.operatorNo}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getPassword())) {
			sql.SET("password = #{tfOperator.password}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getIsDown())) {
			sql.SET("is_down = #{tfOperator.isDown}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getUpdateBy())) {
			sql.SET("update_by = #{tfOperator.updateBy}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getUpdateTime())) {
			sql.SET("update_time = #{tfOperator.updateTime}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getRoleNo())) {
			sql.SET("role_no = #{tfOperator.roleNo}");
		}
		if (EmptyUtils.isNotEmpty(tfOperator.getPropertyGroupNo())) {
			sql.SET("property_group_no = #{tfOperator.propertyGroupNo}");
		}
		sql.WHERE("operator_id = #{tfOperator.operatorId}");
		return sql.toString();
	}
	
	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	public String updateRole(@Param("role") TFRole role) {
		SQL sql = new SQL();// jdbcType为数据库字段类型
		sql.UPDATE("t_f_role");
		if (EmptyUtils.isNotEmpty(role.getRoleName())) {
			sql.SET("role_name = #{role.roleName}");
		}
		if (EmptyUtils.isNotEmpty(role.getRemarks())) {
			sql.SET("remarks = #{role.remarks}");
		}
		if (EmptyUtils.isNotEmpty(role.getIsDown())) {
			sql.SET("is_down = #{role.isDown}");
		}
		if (EmptyUtils.isNotEmpty(role.getUpdateBy())) {
			sql.SET("update_by = #{role.updateBy}");
		}
		if (EmptyUtils.isNotEmpty(role.getUpdateTime())) {
			sql.SET("update_time = #{role.updateTime}");
		}
		sql.WHERE("role_id = #{role.roleId}");
		return sql.toString();
	}
	
	/**
	 * 修改物业
	 * @param property
	 * @return
	 */
	public String updateProperty(@Param("property") TFProperty property) {
		SQL sql = new SQL();// jdbcType为数据库字段类型
		sql.UPDATE("t_f_property");
		if (EmptyUtils.isNotEmpty(property.getPropertyName()))
			sql.SET("property_name = #{property.propertyName}");
		if (EmptyUtils.isNotEmpty(property.getContacts()))
			sql.SET("contacts = #{property.contacts}");
		if (EmptyUtils.isNotEmpty(property.getAddress()))
			sql.SET("address = #{property.address}");
		if (EmptyUtils.isNotEmpty(property.getTelephone()))
			sql.SET("telephone = #{property.telephone}");
		if (EmptyUtils.isNotEmpty(property.getProvince()))
			sql.SET("province = #{property.province}");
		if (EmptyUtils.isNotEmpty(property.getCity()))
			sql.SET("city = #{property.city}");
		if (EmptyUtils.isNotEmpty(property.getDistrict()))
			sql.SET("district = #{property.district}");
		if (EmptyUtils.isNotEmpty(property.getUpdateBy()))
			sql.SET("update_by = #{property.updateBy}");
		if (EmptyUtils.isNotEmpty(property.getUpdateTime()))
			sql.SET("update_time = #{property.updateTime}");
		sql.SET("property_group_no = #{property.propertyGroupNo}");
		sql.WHERE("property_id = #{property.propertyId}");
		return sql.toString();
	}
	
	/**
	 * 修改物业根据物业组No
	 * @param property
	 * @param ids
	 * @return
	 */
	public String updatePropertyByGroupNo(@Param("property") TFProperty property, @Param("ids") String ids[]) {
		String sql = "UPDATE \r\n" + "  t_f_property\r\n" + "SET\r\n"
				+ "  property_group_no = #{property.propertyGroupNo},\r\n" + "  update_by = 1,\r\n"
				+ "  update_time =\" " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())
				+ "\" WHERE property_id IN (";
		StringBuilder sb = new StringBuilder(sql);
		for (int i = 0; i < ids.length; i++) {
			if (i == ids.length - 1) {
				sb.append(ids[i] + ")");
			} else {
				sb.append(ids[i] + ",");
			}
		}
		return sb.toString();
	}
}
