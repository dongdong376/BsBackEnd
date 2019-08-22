package com.jca.facilitymanage.controller.device;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.jca.databeans.pojo.TFArea;
import com.jca.databeans.pojo.TFDevice;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.pojo.TFProperty;
import com.jca.databeans.pojo.TFDevice.TFDeviceBuilder;
import com.jca.databeans.vo.AddDeviceVo;
import com.jca.databeans.vo.UpdateDeviceVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.DateUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.RedisAPI;
import com.jca.facilitymanage.service.device.TFDeviceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/device/deviceInfo")
public class TFDeviceController {

	@Resource
	private TFDeviceService tFDeviceService;
	@Resource
	private RedisAPI redisAPI;

	/**
	 * 查询所有设备
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询所有设备信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/Alldevice", produces = "application/json;charset=UTF-8")
	public Result Alldevice(HttpServletRequest request,
			@ApiParam("物业No") @RequestParam(required = false) String propertyNoVo,
			@ApiParam("区域编号") @RequestParam(value = "areaNo", required = false) String areaNoVo) {
		try {
			long start = System.currentTimeMillis();
			if (request.getParameter("propertyNo") != null)
				propertyNoVo = request.getParameter("propertyNo");
			List<TFDevice> devices = tFDeviceService.findAllDevice(propertyNoVo, areaNoVo);
			int online = 0;
			int error = 0;
			int stop = 0;
			for (int i = 0; i < devices.size(); i++) {
				devices.get(i).setIndex((i + 1));
				if (EmptyUtils.isEmpty(devices.get(i).getAreaNo()))
					devices.get(i).setAreaName("该区域未绑定设备");
				;
				if (EmptyUtils.isNotEmpty(devices.get(i).getProperty()))
					devices.get(i).setPropertyNo(devices.get(i).getProperty().getPropertyName());
				if (devices.get(i).getState().equals("在线"))
					online++;
				if (devices.get(i).getState().equals("停用"))
					stop++;
				if (devices.get(i).getState().equals("异常"))
					error++;
			}
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(devices)
					.withJsonData(Temp.builder().onLine(online).error(error).stop(stop).total(devices.size()).build())
					.withJsonSum(devices.size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!").withData(e.getMessage());
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class Temp {
		Integer onLine;
		Integer error;
		Integer stop;
		Integer total;
	}

	/**
	 * 查询某个设备信息
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取修改设备信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/getOneDivce", produces = "application/json;charset=UTF-8")
	public Result getOneDivce(@RequestParam("id") String no) {
		TFDevice devices = null;
		try {
			devices = tFDeviceService.getOneDevice(no);
			if (EmptyUtils.isEmpty(devices))
				return Result.error("无设备信息");
			return Result.success("查询成功").withData(devices);
		} catch (Exception e) {
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!,请联系");
		}
	}

	/**
	 * 修改设备信息
	 * 
	 * @param deviceVo
	 * @return
	 */
	@ApiOperation(value = "修改设备信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/updateDivceInfo", produces = "application/json;charset=UTF-8")
	public Result updateDivceInfo(@RequestParam("json") String deviceVoParam, HttpServletRequest request) {
		String usertoken = request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("无token!");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆");
		UpdateDeviceVo deviceVo = JSON.parseObject(deviceVoParam, UpdateDeviceVo.class);
		TFDevice devices = null;
		try {
			if (EmptyUtils.isEmpty(deviceVo.getFaceName()) || EmptyUtils.isEmpty(deviceVo.getAreaNo())
					|| EmptyUtils.isEmpty(deviceVo.getFaceInOut()) || EmptyUtils.isEmpty(deviceVo.getState())
					|| EmptyUtils.isEmpty(deviceVo.getPropertyNo()))
				return Result.error("信息不完整!");
			long start = System.currentTimeMillis();
			TFDevice device = new TFDevice();
			device.setUpdateBy(currentUser.getOperatorId().toString());
			device.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			SetUpValue(deviceVo, device);
			TFDevice updevices = tFDeviceService.updateDevice(device);
			if (EmptyUtils.isNotEmpty(updevices))
				return Result.success("修改成功!").withData(updevices);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("修改失败!").withData(devices);
		} catch (Exception e) {
			log.error("error Info" + e.getMessage());
			return Result.error("存在异常!,请联系");
		}
	}

	/**
	 * 移除设备信息
	 * 
	 * @param idsParam
	 * @return
	 */
	@ApiOperation(value = "删除设备信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/removeDeviceInfo", produces = "application/json;charset=UTF-8")
	public Result removeDevice(@RequestParam("ids") String idsParam) {
		try {
			String ids[] = idsParam.split(",");
			long start = System.currentTimeMillis();
			Integer result = tFDeviceService.removeDeviceByArray(ids);
			long end = System.currentTimeMillis();
			if (result > 0)
				return Result.success("删除成功!");
			log.info("耗时==>" + (end - start));
			return Result.error("删除失败!");
		} catch (Exception e) {
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!,请联系");
		}
	}

	/**
	 * 添加设备信息
	 * 
	 * @param deviceVo
	 * @return
	 */
	@ApiOperation(value = "添加设备信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/AddDivceInfo", produces = "application/json;charset=UTF-8")
	public Result AddDivceInfo(@RequestParam("json") String deviceVoParam, HttpServletRequest request,
			@RequestParam(required = false) String token, @RequestParam(required = false) String propertyNo) {
		String usertoken = StringUtils.isEmpty(request.getHeader("usertoken")) ? token : request.getHeader("usertoken");
		if (EmptyUtils.isEmpty(usertoken))
			return Result.error("你还未登陆!");
		TFOperator currentUser = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
		if (EmptyUtils.isEmpty(currentUser))
			return Result.error("你还未登陆!");
		AddDeviceVo deviceVo = JSON.parseObject(deviceVoParam, AddDeviceVo.class);
		try {
			if (EmptyUtils.isEmpty(deviceVo.getFaceName()) || EmptyUtils.isEmpty(deviceVo.getPcNo())
					|| EmptyUtils.isEmpty(deviceVo.getFaceNo()) || EmptyUtils.isEmpty(deviceVo.getFaceInOut())
					|| EmptyUtils.isEmpty(deviceVo.getAreaNo()) || EmptyUtils.isEmpty(deviceVo.getState())
					|| EmptyUtils.isEmpty(deviceVo.getFaceIp()))
				return Result.error("信息不完整!");
			TFDevice device = new TFDevice();
			device.setState(deviceVo.getState() == "是" ? "1" : "0");
			device.setCreateBy(currentUser.getOperatorName().toString());
			device.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			device.setPropertyNo(StringUtils.isEmpty(request.getParameter("propertyNo")) ? propertyNo
					: request.getParameter("propertyNo"));
			// 设置值
			SetAddValue(deviceVo, device);
			device.setFaceNo(
					StringUtils.isEmpty(deviceVo.getFaceNo()) ? DateUtil.format(new Date(), "yyyyMMddHHmmssSSS")
							: deviceVo.getFaceNo());
			if (checkNo(StringUtils.isEmpty(request.getParameter("propertyNo")) ? propertyNo
					: request.getParameter("propertyNo"), null, device.getFaceIp()))
				return Result.error("在此物业下已经存在设备,不能有相同的IP,无法添加!");
			TFDevice updevices = tFDeviceService.save(device);
			if (EmptyUtils.isNotEmpty(updevices))
				return Result.success("添加成功!").withData(updevices);
			return Result.success("添加失败!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:" + e.getMessage());
			return Result.error("存在异常!,请联系");
		}
	}

	/**
	 * 设置值
	 * 
	 * @param deviceVo
	 * @param device
	 */
	private void SetUpValue(UpdateDeviceVo deviceVo, TFDevice device) {
		BeanUtils.copyProperties(deviceVo, device);
	}

	/**
	 * 设置值
	 * 
	 * @param deviceVo
	 * @param device
	 */
	private void SetAddValue(AddDeviceVo deviceVo, TFDevice device) {
		BeanUtils.copyProperties(deviceVo, device);
	}

	/**
	 * 
	 * @param no物业No
	 * @param id
	 *            设备ID
	 * @param faceIp
	 * @return
	 */
	private boolean checkNo(String no, Integer id, String faceIp) {
		TFDevice device = tFDeviceService.getByCondition(TFDevice.builder().propertyNo(no).faceIp(faceIp).build());
		if (EmptyUtils.isEmpty(device)) {
			return false;
		} else {
			if (EmptyUtils.isNotEmpty(id)) {
				if (id != device.getFaceId()) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
	}
}
