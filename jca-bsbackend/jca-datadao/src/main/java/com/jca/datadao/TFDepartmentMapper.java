package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFDepartment;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.DeleteSQLProvider;

public interface TFDepartmentMapper extends BaseSqlProvider<TFDepartment> {
	@DeleteProvider(type=DeleteSQLProvider.class,method="deleteRoleByArray")
	Integer removeDepInfo(@Param("paramIds")String[] paramIds);
	List<TFDepartment> findDepInfo(@Param("ids")String []ids,@Param("name")String name);
}
