package com.jca.datadao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import com.jca.datacommon.SystemLog;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.InsertSQLProvider;

/**
 * 日志Mapper
 * @author Administrator
 *
 */
public interface SystemLogMapper extends BaseSqlProvider<SystemLog> {
    int deleteByPrimaryKey(String id);
    @InsertProvider(method="insertLog",type=InsertSQLProvider.class)
    Integer insert(@Param(value="record")SystemLog record);

    int insertSelective(SystemLog record);

    SystemLog selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(SystemLog record);

    Integer updateByPrimaryKey(SystemLog record);
    
    Long count();
}