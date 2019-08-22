package com.jca.adminlogin.service;

import java.util.Map;

import org.quartz.JobExecutionContext;

import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseService;

public interface AdminService extends BaseService<TFOperator> {
	Map login(TFOperator tfOperatorBuilder);
	TFOperator updatePassWord(TFOperator operator);
	void checkExpirTime(JobExecutionContext context);
}
