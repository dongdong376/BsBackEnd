package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jca.databeans.vo.PropertyGroupVo;

public interface PropertyGroupVoMapper {
	@Select("SELECT fgp.property_group_id AS propertyGroupId,fgp.property_group_no AS propertyGroupNo, "
			+ "fgp.remarks,fgp.is_down AS isDown,fgp.property_group_name AS propertyGroupName,\r\n" + 
			" fgp.create_time AS createTime,fo.operator_name AS operatorName,fr.role_name AS createName  "
			+ ",(SELECT COUNT(0) FROM t_f_property fp WHERE fp.property_group_no=fgp.property_group_no ) AS propertyCount "
			+ "FROM t_f_property_group fgp\r\n" + 
			"INNER JOIN t_f_operator fo ON fgp.create_by=fo.operator_id\r\n" + 
			"INNER JOIN t_f_role fr ON fo.role_no=fr.role_no")
	List<PropertyGroupVo> findAllGroup();
	@Select("\r\n" + 
			"SELECT fgp.property_group_id AS propertyGroupId,fgp.property_group_no AS propertyGroupNo, "
			+ "fgp.remarks,fgp.is_down AS isDown,fgp.property_group_name AS propertyGroupName,\r\n" + 
			" fgp.create_time AS createTime,fo.operator_name AS operatorName,fr.role_name AS createName  "
			+ "FROM t_f_property_group fgp\r\n" + 
			"LEFT JOIN t_f_operator fo ON fgp.create_by=fo.operator_id\r\n" + 
			"LEFT JOIN t_f_role fr ON fo.role_no=fr.role_no \r\n" + 
			"WHERE fgp.property_group_id=#{id}")
	PropertyGroupVo getOneGroup(@Param("id")Integer id);
}
