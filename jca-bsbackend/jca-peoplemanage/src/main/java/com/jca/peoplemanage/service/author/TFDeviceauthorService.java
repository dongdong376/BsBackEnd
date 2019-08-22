package com.jca.peoplemanage.service.author;

import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.datacommon.base.BaseService;

public interface TFDeviceauthorService extends BaseService<TFDeviceauthor> {
	Integer updateAuthor(String []ids);
}
