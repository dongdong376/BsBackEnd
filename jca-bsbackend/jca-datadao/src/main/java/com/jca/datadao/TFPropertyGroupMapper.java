package com.jca.datadao;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;

public interface TFPropertyGroupMapper extends BaseSqlProvider<TFPropertyGroup> {
	@Select("SELECT property_group_id, property_group_no, property_group_name, is_down,remarks, create_by,"
			+ " create_time, update_by, update_time FROM t_f_property_group pg WHERE pg.property_group_no=#{groupNo}")
	TFPropertyGroup getSingleOneByCondition(@Param(value = "groupNo") String groupNo);
	
	@Update("update t_f_property_group set property_group_name=#{group.propertyGroupName},"
			+ "remarks=#{group.remarks},update_by=#{group.updateBy},update_time=#{group.updateTime} "
			+ "where property_group_id=#{group.propertyGroupId}")
	Integer updateGroupInfo(@Param("group")TFPropertyGroup group);
	
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer removeGroupInfoByArray(@Param("paramIds")String []ids);
}
