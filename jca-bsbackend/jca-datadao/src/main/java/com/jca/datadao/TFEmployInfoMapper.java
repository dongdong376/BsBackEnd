package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;

public interface TFEmployInfoMapper extends BaseSqlProvider<TFEmployInfo> {
	List<TFEmployInfo> findAllEmpInfo(@Param("emp")TFEmployInfo employInfo);
	TFEmployInfo getOneEmpInfo(@Param("employId")Integer employId);
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer removeEmpInfo(@Param("paramIds")String[] paramIds);
	Integer updateTFEmployInfoInfo(@Param("TFEmployInfo")TFEmployInfo info);
	Integer updateEmpInfo(@Param("param")Map<String, Object> param);
	List<TFEmployInfo> findDeEmpInfo(@Param("ids")String[] paramIds);
	List<TFEmployInfo> selectisPresentRecord(@Param("type")Integer type,@Param("name")String name);
	List<TFEmployInfo> selectEmpInfoByDepOrName(@Param("param")Map<String, Object> param);
}
