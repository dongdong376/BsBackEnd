package com.jca.peoplemanage.service.author;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFDeviceauthorMapper;

@Service
public class TFDeviceauthorServiceImpl extends BaseServiceImpl<TFDeviceauthorMapper, TFDeviceauthor>
		implements TFDeviceauthorService {
	@Resource
	private TFDeviceauthorMapper tFDeviceauthorMapper;
	
	@Override
	public TFDeviceauthor save(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor updateById(TFDeviceauthor e) {
		Integer result=tFDeviceauthorMapper.updateByPrimaryKeySelective(e);
		if(result>0)
			return tFDeviceauthorMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFDeviceauthor> listByCondition(TFDeviceauthor e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor getByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateAuthor(String[] ids) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("isDown", 2);
		param.put("nos", ids);
		return tFDeviceauthorMapper.updateAuthor(param);
	}

}
