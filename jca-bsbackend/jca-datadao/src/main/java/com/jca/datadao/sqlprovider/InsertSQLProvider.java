package com.jca.datadao.sqlprovider;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import com.jca.databeans.pojo.Test;
import com.jca.datacommon.SystemLog;

/**
 * 有关插入
 * @author Administrator
 *
 */
public class InsertSQLProvider {
	public String insertTest(Map param) {
		StringBuffer sb=new StringBuffer();
		Test test=(Test) param.get("test");
		sb.append("insert into testinfo (infoName) values");
		sb.append(" ('"+test.getInfoName()+"')");
		//MessageFormat mf=new MessageFormat("");
		return sb.toString();
	}
	
	public String insertLog(Map param) {
		StringBuffer sb=new StringBuffer();
		SystemLog log=(SystemLog) param.get("record");
		sb.append("insert into log4j.logtable ("
				+ "				  `description`,\r\n" + 
				"				  `method`,\r\n" + 
				"				  `logType`,\r\n" + 
				"				  `requestIp`,\r\n" + 
				"				  `exceptioncode`,\r\n" + 
				"				  `exceptionDetail`,\r\n" + 
				"				  `params`,\r\n" + 
				"				  `createBy`,\r\n" + 
				"				  `createDate`\r\n" + 
				"				)  values");
		sb.append(" ('"+log.getDescription()+"','"+log.getMethod()+"'");
		sb.append(" ,"+log.getLogType()+",'"+log.getRequestIp()+"'");
		sb.append(" ,'"+log.getExceptioncode()+"','"+log.getExceptionDetail()+"'");
		sb.append(" ,'"+log.getParams()+"','"+log.getCreateBy()+"'");
		sb.append(" ,now())");
		return sb.toString();
	}
	
	public String insertBySQL(@Param("SQL")String sql) {
		return sql;
	}
}
