package com.jca.systemset.service.resource;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFRoleMenuResource;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFRoleMenuResourceMapper;

@Service
public class TFRoleMenuResourceServiceImpl extends BaseServiceImpl<TFRoleMenuResourceMapper, TFRoleMenuResource> implements TFRoleMenuResourceService {
	@Resource
	private TFRoleMenuResourceMapper tFRoleMenuResourceMapper;
	
	@Override
	public TFRoleMenuResource save(TFRoleMenuResource e) {
		Integer result=tFRoleMenuResourceMapper.insert(e);
		if(result>0) {
			e=tFRoleMenuResourceMapper.selectOneByCriteria(e);
		}
		return e;
	}

	@Override
	public TFRoleMenuResource updateById(TFRoleMenuResource e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFRoleMenuResource e) {
		Integer reuslt=tFRoleMenuResourceMapper.deleteByCriteria(e);
		if(reuslt>0) {
			return true;
		}
		return false;
	}

	@Override
	public Long countByCondition(TFRoleMenuResource e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFRoleMenuResource> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFRoleMenuResource> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFRoleMenuResource> listByCondition(TFRoleMenuResource e) {
		return tFRoleMenuResourceMapper.selectByCriteria(e);
	}

	@Override
	public Page<TFRoleMenuResource> listByCondition(TFRoleMenuResource e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFRoleMenuResource getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFRoleMenuResource getByCondition(TFRoleMenuResource e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFRoleMenuResource getOneInfo(String roleNo, Integer menuId) {
		return tFRoleMenuResourceMapper.getSingleInfo(roleNo, menuId);
	}

}
