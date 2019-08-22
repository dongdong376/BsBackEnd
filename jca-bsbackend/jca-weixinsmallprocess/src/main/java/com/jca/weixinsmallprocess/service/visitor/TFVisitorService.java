package com.jca.weixinsmallprocess.service.visitor;

import java.util.List;

import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.base.BaseService;

public interface TFVisitorService extends BaseService<TFVisitor> {
	List<TFVisitor> findAllVisitorRecord(Object ...objects);
}
