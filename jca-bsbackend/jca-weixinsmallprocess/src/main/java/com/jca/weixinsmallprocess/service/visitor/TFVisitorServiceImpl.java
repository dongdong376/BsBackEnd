package com.jca.weixinsmallprocess.service.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFVisitor;
import com.jca.datacommon.base.BaseServiceImpl;
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
		Map<String, Object> param=new HashMap<>();
		param.put("propertyNo", objects[0]);
		param.put("visitorId", objects[1]);
		return tFVisitorMapper.selectAllVisitorRecord(param);
	}

}
