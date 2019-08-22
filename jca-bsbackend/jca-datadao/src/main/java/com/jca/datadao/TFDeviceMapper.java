package com.jca.datadao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jca.databeans.pojo.TFDevice;
import com.jca.datacommon.base.BaseSqlProvider;

public interface TFDeviceMapper extends BaseSqlProvider<TFDevice> {
	List<TFDevice> findAllDevice(@Param("param")Map<String, Object> param);
	TFDevice getOneDevice(@Param("faceNo")String faceNo);
	Integer updateDeviceInfo(@Param(value="TFDevice")TFDevice device);
	Integer removeDeviceByArray(@Param("ids")String [] ids);
}
