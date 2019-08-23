package com.jca.weixinsmallprocess.service.visitor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFVisitorMapper;

@Service
public class TFVisitorServiceImpl extends BaseServiceImpl<TFVisitorMapper, TFVisitor> implements TFVisitorService {

	@Resource
	private TFVisitorMapper tFVisitorMapper;

	@Override
	public TFVisitor save(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFVisitor updateById(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFVisitor> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFVisitor> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFVisitor> listByCondition(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFVisitor> listByCondition(TFVisitor e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFVisitor getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFVisitor getByCondition(TFVisitor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFVisitor> findAllVisitorRecord(Object... objects) {
		Map<String, Object> param = new HashMap<>();
		param.put("propertyNo", objects[0]);
		param.put("visitorId", objects[1]);
		return tFVisitorMapper.selectAllVisitorRecord(param);
	}

	@MethodLog("审核")
	@Transactional
	@Override
	public TFVisitor updateVisitor(Object... objects) {
		String state = String.valueOf(objects[1]);
		// if(state==1)审核状态(0:未审核,1:已通过,2:已拒绝)
		Integer result = tFVisitorMapper
				.updateByPrimaryKeySelective(TFVisitor.builder().visitorId(Integer.valueOf(objects[0].toString()))
						.checkDate(new Date()).checkState(StringUtils.isEmpty(state) ? "0" : state)
						.confuseReason(String.valueOf(objects[2])).build());
		if (result > 0)
			return tFVisitorMapper.selectByPrimaryKey(Integer.valueOf(objects[0].toString()));
		return null;
	}

}
