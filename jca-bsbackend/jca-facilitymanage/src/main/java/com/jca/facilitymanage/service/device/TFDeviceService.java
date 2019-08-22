package com.jca.facilitymanage.service.device;

import java.util.List;

import com.jca.databeans.pojo.TFDevice;
import com.jca.datacommon.base.BaseService;

public interface TFDeviceService extends BaseService<TFDevice>{
	List<TFDevice> findAllDevice(Object ...objects);
	TFDevice updateDevice(TFDevice device);
	Integer removeDeviceByArray(String [] ids);
	TFDevice getOneDevice(String faceNo);
}
