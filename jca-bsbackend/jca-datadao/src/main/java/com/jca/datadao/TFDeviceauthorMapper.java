package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;
import com.jca.datadao.sqlprovider.InsertSQLProvider;

public interface TFDeviceauthorMapper extends BaseSqlProvider<TFDeviceauthor> {
	Integer updateAuthor(@Param("param")Map<String, Object> map);
	List<TFDeviceauthor> selectDeviceAuthor(@Param("param")Map<String, Object> param);
	@InsertProvider(type=InsertSQLProvider.class,method="insertBySQL")
	Integer insertAuthor(@Param("SQL")String sql);
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer removeAuthor(@Param(value="paramIds")String paramIds[]);	
}
