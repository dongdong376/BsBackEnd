package com.jca.peoplemanage.service.department;

import com.jca.databeans.pojo.TFDepartment;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.tool.Result;

public interface TFDepartmentService extends BaseService<TFDepartment> {
	Result removeDepInfo(String [] ids)throws Exception;
}
