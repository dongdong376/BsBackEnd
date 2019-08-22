package com.jca.facilitymanage.service.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFDevice;
import com.jca.databeans.pojo.TFProperty;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFDeviceMapper;
import com.jca.datadao.TFPropertyMapper;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.EmptyUtils;

@Service
public class TFDeviceServiceImpl extends BaseServiceImpl<TFDeviceMapper, TFDevice> implements TFDeviceService {

	@Resource
	private TFDeviceMapper tFDeviceMapper;
	@Resource
	private TFPropertyMapper propertyMapper;

	@Override
	public List<TFDevice> findAllDevice(Object ...objects) {
		Map<String, Object> param=new HashMap<>();
		param.put("propertyNo", objects[0]);
		param.put("areaNo", objects[1]);
		List<TFDevice> devices = tFDeviceMapper.findAllDevice(param);
		devices.stream().forEach(d -> {
			d.setState(switchState(d.getState()));
			d.setIsOnline(Integer.valueOf(d.getFaceInOut()) == 0 ? "在线" : "离线");
			d.setFaceInOut(d.getFaceInOut().equals("1") ? "进" : "出");
		});
		return devices;
	}

	private String switchState(String state) {
		switch (state) {
		case "1":
			state = "停用";
			break;
		case "2":
			state = "异常";
			break;
		default:
			state = "在线";
			break;
		}
		return state;
	}

	@MethodLog("添加设备信息")
	@SuppressWarnings("unchecked")
	@Override
	public TFDevice save(TFDevice e) {		
		Integer result = tFDeviceMapper.insert(e);
		if (result > 0)
			return tFDeviceMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public TFDevice updateById(TFDevice e) {
		Integer result = tFDeviceMapper.updateByPrimaryKeySelective(e);
		if (result > 0)
			return tFDeviceMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFDevice e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFDevice e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDevice> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDevice> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDevice> listByCondition(TFDevice e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFDevice> listByCondition(TFDevice e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDevice getById(Integer id) {
		TFDevice d = tFDeviceMapper.selectByPrimaryKey(id);
		d.setIsOnline(Integer.valueOf(d.getFaceInOut()) == 0 ? "在线" : "离线");
		return d;
	}

	@Override
	public TFDevice getByCondition(TFDevice e) {
		return tFDeviceMapper.selectOneByCriteria(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TFDevice updateDevice(TFDevice device) {
		List<TFProperty> list = (List<TFProperty>) CacheUtil.get("properties");
		list.stream().forEach(p -> {
			if (device.getPropertyNo().equals(p.getPropertyNo()))
				device.setSecretKey(p.getSecretKey());
		});
		Integer result = tFDeviceMapper.updateDeviceInfo(device);
		if (result > 0)
			return tFDeviceMapper.selectOneByCriteria(device);
		return null;
	}

	@Override
	public Integer removeDeviceByArray(String[] ids) {
		return tFDeviceMapper.removeDeviceByArray(ids);
	}
	@MethodLog(value="获取某台设备")
	@Override
	public TFDevice getOneDevice(String faceNo) {
		TFDevice d = tFDeviceMapper.getOneDevice(faceNo);
		if (EmptyUtils.isNotEmpty(d)) {
			d.setIsOnline(Integer.valueOf(d.getFaceInOut()) == 0 ? "在线" : "离线");
		}
		return d;
	}
}
