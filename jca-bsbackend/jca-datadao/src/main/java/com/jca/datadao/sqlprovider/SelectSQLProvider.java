package com.jca.datadao.sqlprovider;

import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.Test;
import com.jca.datacommon.tool.StringUtils;

public class SelectSQLProvider {
	public Test selectTestByName() {
		return null;
	}

	public String selectRoleByRoleName(@Param(value = "roleName") String roleName) {
		String sql = "SELECT role_id AS roleId,fr.role_no AS roleNo,role_name as roleName,remarks,fr.is_down AS isDown,"
				+ "(SELECT COUNT(0) FROM t_f_operator fo WHERE fo.role_no=fr.role_no) AS operatorSum,fr.create_time AS createTime,"
				+ "update_time as updateTime FROM t_f_role fr where fr.role_name like \"%" + roleName + "%\"";
		return sql;
	}

	public String selectRoleByCondition(@Param(value = "roleIds") String[] roleIds) {
		String sql = "SELECT role_id AS roleId,fr.role_no AS roleNo,role_name as roleName,remarks,fr.is_down AS isDown,"
				+ "(SELECT COUNT(0) FROM t_f_operator fo WHERE fo.role_no=fr.role_no) AS operatorSum,fr.create_time AS createTime,"
				+ "update_time as updateTime FROM t_f_role fr";
		StringBuilder sb = new StringBuilder(sql);
		if (roleIds.length > 1) {
			sb.append(" where fr.role_id in (");
			for (int i = 0; i < roleIds.length; i++) {
				if (i != roleIds.length - 1) {
					sb.append(roleIds[i] + ",");
				} else {
					sb.append(roleIds[i] + ")");
				}
			}
		} else {
			sb.append(" where fr.role_id =" + roleIds[0]);
		}
		return sb.toString();
	}

	public String countRecordInfo(@Param("type") String type, @Param("propertyNo") String propertyNo) {
		if (type.equals("1")) {
			type = " AND DATE(fe.`record_date_time`)=DATE(NOW())";
		} else if (type.equals("2")) {
			type = " AND YEARWEEK(DATE_FORMAT(fe.`record_date_time`,'%Y-%m-%d')) = YEARWEEK(NOW())";
		} else if (type.equals("3")) {
			type = " AND DATE_FORMAT( fe.record_date_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
		} else {
			type = " 1=1 ";
		}
		String condition = (StringUtils.isEmpty(propertyNo) ? ""
				: (propertyNo.equals("全部") ? "" : ("AND fe.property_name='" + propertyNo + "'")));
		String sql = "SELECT \r\n" + "  fa.area_name AS areaName,\r\n" + "  (SELECT \r\n" + "    COUNT(0) \r\n"
				+ "  FROM\r\n" + "    `t_f_event_record` fe \r\n" + "  WHERE fa.area_no = fe.`area_name` \r\n"
				+ "    AND fe.`face_in_out` = 1 " + condition + type + ") AS inSum,\r\n" + "  (SELECT \r\n"
				+ "    COUNT(0) \r\n" + "  FROM\r\n" + "    `t_f_event_record` fe \r\n"
				+ "  WHERE fa.area_no = fe.`area_name` \r\n" + "    AND fe.`face_in_out` = 2 " + condition + type
				+ ") AS outSum \r\n" + "FROM\r\n" + "  t_f_area fa ";
		return sql;
	}
}
