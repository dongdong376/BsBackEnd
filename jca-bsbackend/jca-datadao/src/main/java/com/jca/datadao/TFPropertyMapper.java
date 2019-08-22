package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import com.jca.databeans.pojo.TFProperty;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;
import com.jca.datadao.sqlprovider.UpdateSQLProvider;

public interface TFPropertyMapper extends BaseSqlProvider<TFProperty> {
	
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer removeProperty(@Param(value="paramIds")String paramIds[]);
	
	@UpdateProvider(type=UpdateSQLProvider.class,method="updateProperty")
	Integer updateProperty(@Param(value="property") TFProperty property);
	
	@UpdateProvider(type=UpdateSQLProvider.class,method="updatePropertyByGroupNo")
	Integer updatePropertyByid(@Param("property")TFProperty property,@Param("ids")String ids[]);
	
	List<Integer> findDeviceCount(@Param("ids")String [] ids);
}
