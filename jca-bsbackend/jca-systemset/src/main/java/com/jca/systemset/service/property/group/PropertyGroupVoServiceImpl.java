package com.jca.systemset.service.property.group;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jca.databeans.vo.PropertyGroupVo;
import com.jca.datadao.PropertyGroupVoMapper;

@Service
public class PropertyGroupVoServiceImpl implements PropertyGroupVoService {
	
	@Resource
	private PropertyGroupVoMapper propertyGroupVoMapper;
	
	
	//@Async
	@Override
	public List<PropertyGroupVo> findAllPropertyGroup() {
		return propertyGroupVoMapper.findAllGroup();
	}

	@Override
	public PropertyGroupVo getOneGroup(Integer id) {
		return propertyGroupVoMapper.getOneGroup(id);
	}

}
