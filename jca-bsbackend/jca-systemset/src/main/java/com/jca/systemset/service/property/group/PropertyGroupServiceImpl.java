package com.jca.systemset.service.property.group;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFPropertyGroupMapper;

@Service
public class PropertyGroupServiceImpl extends BaseServiceImpl<TFPropertyGroupMapper, TFPropertyGroup> implements PropertyGroupService {
	@Resource
	private TFPropertyGroupMapper propertyGroupMapper;

	@Override
	public TFPropertyGroup save(TFPropertyGroup e) {
		Integer result=propertyGroupMapper.insert(e);
		if(result>0)
			return propertyGroupMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public TFPropertyGroup updateById(TFPropertyGroup e) {
		
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFPropertyGroup e) {
		
		return null;
	}

	@Override
	public Long countByCondition(TFPropertyGroup e) {
		
		return null;
	}
	//@Cacheable(value="myCache")
	//@DataSource(DataSource.jcafaceone)
	@Override
	public List<TFPropertyGroup> listAll(String orderBy) {
		return propertyGroupMapper.selectAll(orderBy);
	}
	@Cacheable(value="myCache")
	@Override
	public List<TFPropertyGroup> listAll() {
		return propertyGroupMapper.selectAll(null);
	}

	@Override
	public List<TFPropertyGroup> listByCondition(TFPropertyGroup e) {
		
		return null;
	}

	@Override
	public Page<TFPropertyGroup> listByCondition(TFPropertyGroup e, PageForm pageForm) {
		
		return null;
	}

	@Override
	public TFPropertyGroup getById(Integer id) {
		
		return null;
	}

	@Override
	public TFPropertyGroup getByCondition(TFPropertyGroup e) {		
		return propertyGroupMapper.selectOneByCriteria(e);
	}

	public TFPropertyGroup getGroupByCondition(String groupNo) {
		return propertyGroupMapper.getSingleOneByCondition(groupNo);
	}

	@Override
	public TFPropertyGroup updateGroupInfo(TFPropertyGroup group) {
		Integer result=propertyGroupMapper.updateGroupInfo(group);
		if(result>0)
			return group=propertyGroupMapper.selectOneByCriteria(group);
		return null;
	}

	@Override
	public boolean removeGroupByArray(String[] ids) {
		if(propertyGroupMapper.removeGroupInfoByArray(ids)>0)
			return true;
		return false;
	}
}
