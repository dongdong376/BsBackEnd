package com.jca.weixinsmallprocess.service.ownerapply;

import java.util.List;

import com.jca.databeans.pojo.TFOwnerApply;
import com.jca.datacommon.base.BaseService;

public interface OwnerApplyService extends BaseService<TFOwnerApply> {
		List<TFOwnerApply> findAllOwnerApply(Object ...objects);
}
