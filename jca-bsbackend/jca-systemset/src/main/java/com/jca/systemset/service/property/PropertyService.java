package com.jca.systemset.service.property;

import java.util.List;

import com.jca.databeans.pojo.TFProperty;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.tool.Result;


public interface PropertyService extends BaseService<TFProperty> {
	List<TFProperty> findProperty();
	Integer removePrpoerty(String propertyIds[]);
	TFProperty updatePropertyInfo(TFProperty property);
	Integer updatePropertyInfo(TFProperty property,String []ids);
	Result propertyDevice(String []ids);
}
