package com.jca.systemset.service.property.group;

import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.datacommon.base.BaseService;

public interface PropertyGroupService extends BaseService<TFPropertyGroup> {
	TFPropertyGroup getGroupByCondition(String groupNo);
	TFPropertyGroup updateGroupInfo(TFPropertyGroup group);
	boolean removeGroupByArray(String []ids);
}
