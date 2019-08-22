package com.jca.systemset.service.property.group;

import java.util.List;

import com.jca.databeans.vo.PropertyGroupVo;

public interface PropertyGroupVoService {
	List<PropertyGroupVo> findAllPropertyGroup();
	PropertyGroupVo getOneGroup(Integer id);
}
