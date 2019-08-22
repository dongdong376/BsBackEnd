package com.jca.facilitymanage.service.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFArea;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFAreaMapper;

@Service
public class TFAreaServiceImpl extends BaseServiceImpl<TFAreaMapper, TFArea> implements TFAreaService {
	
	@Resource
	private TFAreaMapper tFAreaMapper;
	
	@Override
	public TFArea save(TFArea e) {
		Integer result=tFAreaMapper.insert(e);
		if(result>0)
			return tFAreaMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public TFArea updateById(TFArea e) {
		Integer result=tFAreaMapper.updateAreaInfo(e);
		if(result>0)
			return tFAreaMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFArea e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFArea e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFArea> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFArea> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFArea> listByCondition(TFArea e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFArea> listByCondition(TFArea e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFArea getById(Integer id) {
		return tFAreaMapper.selectByPrimaryKey(id);
	}

	@Override
	public TFArea getByCondition(TFArea e) {
		return tFAreaMapper.selectOneByCriteria(e);
	}

	@Override
	public List<TFArea> findAllArea(String key) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("key", key);
		return tFAreaMapper.findAllArea(param);
	}

	@Override
	public Integer removeAreaByArray(String[] ids) {
		return tFAreaMapper.removeAreaByArray(ids);
	}

	@Override
	public List<TFArea> findCheckAreaList(String[] ids) {
		Map<String, Object> param=new HashMap<>();
		param.put("ids", ids);
		return tFAreaMapper.findAllArea(param);
	}	

}
