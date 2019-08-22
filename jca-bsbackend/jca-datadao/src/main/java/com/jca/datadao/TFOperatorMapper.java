package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datadao.sqlprovider.UpdateSQLProvider;

public interface TFOperatorMapper extends BaseSqlProvider<TFOperator> {
	@Select("select * from t_f_operator where operator_id=#{opId}")
	public TFOperator getOPeratorById(@Param("opId")Integer opId);

	@Select("SELECT \r\n" + "  `operator_id`,\r\n" + "  `operator_no`,\r\n" + "  `operator_name`,\r\n"
			+ "  `password`,\r\n" + "  `role_no`,\r\n" + "  `property_group_no`,\r\n" + "  `is_enable`,\r\n" + "  (\r\n"
			+ "    CASE\r\n" + "      WHEN is_down = 0 \r\n" + "      THEN '停用' \r\n" + "      WHEN is_down = 1 \r\n"
			+ "      THEN '启用' \r\n" + "    END\r\n" + "  ) AS isDown,\r\n" + "  `create_by`,\r\n"
			+ "  `create_time`,\r\n" + "  `update_by`,\r\n" + "  IFNULL(`update_time`, '--') \r\n" + "FROM\r\n"
			+ "  `t_f_operator` \r\n" + "ORDER BY operator_id ASC ")
	public List<TFOperator> findOperatorAll();
	
	@UpdateProvider(type=UpdateSQLProvider.class,method="updateOperator")
	public Integer updateOperatorInfo(@Param("tfOperator")TFOperator tfOperator);
	
	@Update("UPDATE jcafaceone.t_f_operator SET PASSWORD = #{password} WHERE operator_id = #{operatorId}")
	public Integer updateOperatorPassWord(@Param("operatorId")Integer operatorId,@Param("password")String password);
}
