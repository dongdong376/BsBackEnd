package com.jca.weixinsmallprocess.service;

import java.util.Map;

import org.quartz.JobExecutionContext;

import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseService;

public interface AdminService extends BaseService<TFEmployInfo> {
	Map login(TFEmployInfo employInfo);
	TFOperator updatePassWord(TFOperator operator);
	void checkExpirTime(JobExecutionContext context);
}
