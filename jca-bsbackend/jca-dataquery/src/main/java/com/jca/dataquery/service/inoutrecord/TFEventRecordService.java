package com.jca.dataquery.service.inoutrecord;

import java.util.List;
import java.util.Map;

import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFEventRecord;
import com.jca.databeans.pojo.TempEntity;
import com.jca.datacommon.base.BaseService;

public interface TFEventRecordService extends BaseService<TFEventRecord> {
	List<TFEventRecord> findAllOrOneRecord(TFEventRecord e,Integer currentPage);
	Map<String, Object> countRecordInfo(String type,String propertyName);
	TempEntity todayOryesterdayFlux(String name);
	List<TempEntity> findAllSituation(String name);
	List<TFEmployInfo> selectCurrentTime(String type,String name);
	void TimerInsert();
	void TimerSelect();
	void TimerUpdate();
}
