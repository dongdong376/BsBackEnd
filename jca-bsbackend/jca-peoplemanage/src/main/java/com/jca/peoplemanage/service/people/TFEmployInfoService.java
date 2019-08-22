package com.jca.peoplemanage.service.people;

import java.util.List;

import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.vo.AddEmpInfoVo;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.tool.Result;

public interface TFEmployInfoService extends BaseService<TFEmployInfo> {
	List<TFEmployInfo> findAllEmpInfo(TFEmployInfo info,Integer currentPage)throws Exception;
	TFEmployInfo addEmployeeInfo(String propertyNo,AddEmpInfoVo empInfoVo,String userNameId,String propertyName)throws Exception;
	Result removeEmpInfo(String [] ids,String opUserId);
	List<TFEmployInfo> findAllEmpInfoByDepOrName(Object ...objects)throws Exception;
}
