package com.jca.facilitymanage.service.author;

import java.util.List;

import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.tool.Result;

public interface TFDeviceauthorService extends BaseService<TFDeviceauthor> {
	List<TFDeviceauthor> findAllAuthor(Object ...objects);
	Result insertAuthor(Object ...objects);
	Integer removeAuthor(String ids[]);
}
