package com.jca.facilitymanage.service.area;

import java.util.List;

import com.jca.databeans.pojo.TFArea;
import com.jca.datacommon.base.BaseService;

public interface TFAreaService extends BaseService<TFArea> {
	List<TFArea> findAllArea(String key);
	Integer removeAreaByArray(String [] ids);
	List<TFArea> findCheckAreaList(String [] ids);
}
