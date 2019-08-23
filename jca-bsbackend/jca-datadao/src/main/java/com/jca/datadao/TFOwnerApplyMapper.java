package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFOwnerApply;
import com.jca.datacommon.base.BaseSqlProvider;

public interface TFOwnerApplyMapper extends BaseSqlProvider<TFOwnerApply> {
	List<TFOwnerApply> selectOwnerApply(@Param("param")Map<?, ?> param);
}
