package com.jca.systemset.service.operator;

import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseService;

public interface TFOperatorService extends BaseService<TFOperator> {
	public TFOperator getOperatorByCondition(Integer opid);
}
