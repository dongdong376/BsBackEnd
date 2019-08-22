package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.base.BaseSqlProvider;

public interface TFVisitorMapper extends BaseSqlProvider<TFVisitor> {
	List<TFVisitor> selectAllVisitorRecord(@Param("param")Map<?, ?> param);
}
