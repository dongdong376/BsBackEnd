package com.jca.adminlogin.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.log.MethodLog;
import com.jca.datadao.TFOperatorMapper;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;

@Service
public class AdminServiceImpl extends BaseServiceImpl<TFOperatorMapper, TFOperator> implements AdminService {
	@Resource
	private TFOperatorMapper tFOperatorMapper;
	@Resource
	private RedisAPI redisAPI;

	@MethodLog(value = "操作用户登录", time = true)
	@Override
	public Map<String, Object> login(TFOperator operator) {
		Map<String, Object> map = new HashMap<String, Object>();
		operator = tFOperatorMapper.selectOneByCriteria(operator);
		try {
			operator.setCreateTime(DateUtil.paseDateformat(operator.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			operator.setUpdateTime(DateUtil.paseDateformat(operator.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			if (EmptyUtils.isNotEmpty(operator)) {
				String userToken = UUIDUtils.generateUUID();
				map.put("operator", operator);
				map.put("userToken", userToken);
				//缓存token
				CacheUtil.cache.put("userToken-" + operator.getOperatorId(), userToken);
				redisAPI.set(userToken, 30 * 60 * 60, JSON.toJSONString(operator));
			}
		} catch (Exception e) {
			map.put("error", e.getMessage());
		}
		return map;
	}
	@MethodLog(value="用户修改密码")
	@Override
	public TFOperator updatePassWord(TFOperator operator) {
		Integer result = tFOperatorMapper.updateOperatorPassWord(operator.getOperatorId(), operator.getPassword());
		if (result > 0)
			return tFOperatorMapper.selectByPrimaryKey(operator.getOperatorId());
		return null;
	}

	// @Scheduled(fixedRate=1000)
	@Override
	public void checkExpirTime(JobExecutionContext context) {
	}
}
