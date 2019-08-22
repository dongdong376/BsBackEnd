package com.jca.adminlogin.timejob;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datatool.RedisAPI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenJon implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 获取trigger名字，trigger 名字+group 是触发器的唯一标识
		log.info(((CronTriggerImpl) ((JobExecutionContextImpl) context).getTrigger()).getName());
		// 获取触发器的时间
		log.info(((CronTriggerImpl) ((JobExecutionContextImpl) context).getTrigger()).getCronExpression());
		// 业务逻辑
		String token = context.getJobDetail().getJobDataMap().get("data1").toString();
		RedisAPI redisAPI=(RedisAPI) context.getJobDetail().getJobDataMap().get("data2");
		TFOperator operator = JSON.parseObject(redisAPI.get(token), TFOperator.class);		
		log.info("用户为==>"+operator.getOperatorName());
		log.info("有效期剩余==>"+redisAPI.ttl(token));
		if (redisAPI.ttl(token) == -1) {
			log.info("token即将过期");
			redisAPI.set(token, 30 * 60, JSON.toJSONString(operator));		
		}
	}

}
