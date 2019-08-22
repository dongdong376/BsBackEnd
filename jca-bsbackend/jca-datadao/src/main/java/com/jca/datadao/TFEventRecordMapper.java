package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.jca.databeans.pojo.TFEventRecord;
import com.jca.databeans.pojo.TempEntity;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.SelectSQLProvider;

public interface TFEventRecordMapper extends BaseSqlProvider<TFEventRecord> {
		List<TFEventRecord> selectAllRecord(@Param("record")TFEventRecord eventRecord);
		@SelectProvider(type=SelectSQLProvider.class,method="countRecordInfo")
		List<TFEventRecord> countRecordInfo(@Param("type")String type);
		TempEntity StrangenessOrResidentSumFlux(@Param("name")String name);	
	    List<TempEntity> selectSumFlux(@Param("name")String name);
	    List<TFEventRecord> updateRecord(@Param("name")String name);
	    List<TFEventRecord> selectRecordDetail();
	    List<TFEventRecord> selectCurrentTime();
}
