package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.jca.databeans.pojo.TFArea;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;
import com.jca.datadao.sqlprovider.SelectSQLProvider;

public interface TFAreaMapper extends BaseSqlProvider<TFArea> {
	List<TFArea> findAllArea(@Param("param")Map<String, Object> param);

	@DeleteProvider(type = DeleteSQLProvider.class, method = "deleteRoleByArray")
	Integer removeAreaByArray(@Param("paramIds") String[] ids);
	
	Integer updateAreaInfo(@Param("TFArea")TFArea tfArea);
	@SelectProvider(type=SelectSQLProvider.class,method="countRecordInfo")
	List<TFArea>  countRecordInfo(@Param("type")String type,@Param("propertyNo")String propertyNo);
}
