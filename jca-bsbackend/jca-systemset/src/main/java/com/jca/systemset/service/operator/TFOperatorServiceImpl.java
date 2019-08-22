package com.jca.systemset.service.operator;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFOperator;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFOperatorMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TFOperatorServiceImpl extends BaseServiceImpl<TFOperatorMapper, TFOperator> implements TFOperatorService {
	@Resource
	private TFOperatorMapper  tFOperatorMapper;
	
	@Override
	public TFOperator save(TFOperator e) {
		int result=tFOperatorMapper.insert(e);
		if(result>0) {
			return tFOperatorMapper.selectOneByCriteria(e);
		}
		return null;
	}

	@Override
	public TFOperator updateById(TFOperator e) {
		Integer result=tFOperatorMapper.updateByPrimaryKeySelective(e);//为空不更新
		if(result>0) {
			return tFOperatorMapper.selectOneByCriteria(e);
		}
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFOperator e) {
		
		return null;
	}

	@Override
	public Long countByCondition(TFOperator e) {
		
		return null;
	}
	//@Cacheable(value="myCache")
	@Override
	public List<TFOperator> listAll(String orderBy) {
		return tFOperatorMapper.selectAll(orderBy);
	}
	//@Cacheable(value="myCache")
	@Override
	public List<TFOperator> listAll() {
		return tFOperatorMapper.findOperatorAll();
	}

	@Override
	public List<TFOperator> listByCondition(TFOperator e) {
		
		return null;
	}

	@Override
	public Page<TFOperator> listByCondition(TFOperator e, PageForm pageForm) {
		
		return null;
	}

	@Override
	public TFOperator getById(Integer id) {		
		return tFOperatorMapper.selectByPrimaryKey(id);
	}

	@Override
	public TFOperator getByCondition(TFOperator e) {
		return tFOperatorMapper.selectOneByCriteria(e);
	}

	@Override
	public TFOperator getOperatorByCondition(Integer opid) {
		
		return tFOperatorMapper.getOPeratorById(opid);
	}

}
