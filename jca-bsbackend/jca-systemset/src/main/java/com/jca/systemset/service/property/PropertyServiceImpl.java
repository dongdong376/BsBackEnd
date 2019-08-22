package com.jca.systemset.service.property;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFProperty;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFPropertyMapper;

@Service
public class PropertyServiceImpl extends BaseServiceImpl<TFPropertyMapper, TFProperty> implements PropertyService {
	@Resource
	private TFPropertyMapper prpertyMapper;
	
	@Override
	public TFProperty save(TFProperty e) {
		Integer result=prpertyMapper.insert(e);
		if(result>0) {
			return e=prpertyMapper.selectOneByCriteria(e);
		}
		return null;
	}

	@Override
	public TFProperty updateById(TFProperty e) {
		prpertyMapper.updateByPrimaryKeySelective(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {

		return null;
	}

	@Override
	public Boolean deleteByCondition(TFProperty e) {

		return null;
	}

	@Override
	public Long countByCondition(TFProperty e) {

		return null;
	}

	@Override
	public List<TFProperty> listAll(String orderBy) {
		return prpertyMapper.selectAll(orderBy);
	}

	@Override
	public List<TFProperty> listAll() {
		return prpertyMapper.selectAll(null);
	}

	@Override
	public List<TFProperty> listByCondition(TFProperty e) {
		return prpertyMapper.selectByCriteria(e);
	}

	@Override
	public Page<TFProperty> listByCondition(TFProperty e, PageForm pageForm) {

		return null;
	}

	@Override
	public TFProperty getById(Integer id) {
		return prpertyMapper.selectByPrimaryKey(id);
	}

	@Override
	public TFProperty getByCondition(TFProperty e) {
		return prpertyMapper.selectOneByCriteria(e);
	}
	//@Async
	@Override
	public List<TFProperty> findProperty() {
		return prpertyMapper.selectAll(null);
	}

	@Override
	public Integer removePrpoerty(String[] propertyIds) {
		return prpertyMapper.removeProperty(propertyIds);
	}

	@Override
	public TFProperty updatePropertyInfo(TFProperty property) {
		Integer result=prpertyMapper.updateProperty(property);
		if(result>0)
			return property=prpertyMapper.selectOneByCriteria(property);
		return null;
	}
	@Override
	public Integer updatePropertyInfo(TFProperty property, String[] ids) {
		return prpertyMapper.updatePropertyByid(property, ids);
	}

	@Override
	public Result propertyDevice(String[] ids) {
		List<Integer>integers=prpertyMapper.findDeviceCount(ids);
		Integer sum=0;
		for (Integer integer : integers) {
			if(integer>=1)
				sum+=integer;
		}
		if(sum>=1)
			return Result.error("物业共与"+sum+"台设备关联,不能删除!");
		return Result.success();
	}

}
