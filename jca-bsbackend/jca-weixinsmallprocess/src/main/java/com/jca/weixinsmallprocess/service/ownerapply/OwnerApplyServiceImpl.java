package com.jca.weixinsmallprocess.service.ownerapply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFOwnerApply;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFOwnerApplyMapper;

@Service
public class OwnerApplyServiceImpl extends BaseServiceImpl<TFOwnerApplyMapper, TFOwnerApply>
		implements OwnerApplyService {
	
	@Resource
	private TFOwnerApplyMapper tFOwnerApplyMapper;

	@Override
	public TFOwnerApply save(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFOwnerApply updateById(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFOwnerApply> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFOwnerApply> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFOwnerApply> listByCondition(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFOwnerApply> listByCondition(TFOwnerApply e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFOwnerApply getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFOwnerApply getByCondition(TFOwnerApply e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFOwnerApply> findAllOwnerApply(Object... objects) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("propertyNo", objects[0]);
		return tFOwnerApplyMapper.selectOwnerApply(param);
	}

}
