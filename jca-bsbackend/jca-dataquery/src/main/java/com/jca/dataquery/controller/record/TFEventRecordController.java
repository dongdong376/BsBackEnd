package com.jca.dataquery.controller.record;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFEventRecord;
import com.jca.databeans.pojo.TempEntity;
import com.jca.databeans.pojo.TFEmployInfo.TFEmployInfoBuilder;
import com.jca.datacommon.enums.ResultEnum;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.dataquery.controller.record.TFEventRecordController.TempInOut;
import com.jca.dataquery.service.inoutrecord.TFEventRecordService;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("出入记录")
@RestController
@RequestMapping("/dataquery")
public class TFEventRecordController {

	@Resource
	private TFEventRecordService tFEventRecordService;

	/**
	 * 查询出入记录信息
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "查询所有或单个记录信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	// @MethodLog(value = "查询所有记录信息")
	@RequestMapping(value = "/findAllRecordInfo", produces = "application/json;charset=UTF-8")
	public Result findAllRecordInfo(HttpServletRequest request,
			@ApiParam(value = "记录的ID") @RequestParam(value = "id", required = false) Integer id,
			@ApiParam(value = "人员") @RequestParam(value = "empName", required = false) String employeeName,
			@ApiParam(value = "进出类型") @RequestParam(value = "inOrOut", required = false) Integer inOrOut,
			@ApiParam(value = "记录类型") @RequestParam(value = "recordType", required = false) String recordType,
			@ApiParam(value = "部门名称") @RequestParam(value = "dept", required = false) String departmentName,
			@ApiParam(value = "人员的no") @RequestParam(value = "empNo", required = false) String employeeNo,
			@ApiParam(value = "记录的开始日") @RequestParam(value = "startDate", required = false) String startDate,
			@ApiParam(value = "记录的结束日") @RequestParam(value = "endDate", required = false) String endDate,
			@ApiParam(value = "当前页码") @RequestParam(value = "currentPage", required = false) Integer currentPage) {
		try {
			long start = System.currentTimeMillis();
			if (EmptyUtils.isEmpty(currentPage))
				currentPage = 1;
			Map<String, Object> map = new HashMap<String, Object>();
			TFEventRecord record = new TFEventRecord();
			record.setEmployName(employeeName);
			record.setFaceInOut(inOrOut);
			record.setRecordType(recordType);
			record.setDepartmentName(departmentName);
			record.setEmployNo(employeeNo);
			record.setStartDate(startDate);
			record.setEndDate(endDate);
			record.setEventRecordId(id);
			record.setPropertyName(request.getParameter("propertyName"));
			List<TFEventRecord> records = tFEventRecordService.findAllOrOneRecord(record, currentPage);
			int i;
			if (currentPage > 1 && currentPage <= ((Page<?>) records).getPages() && CacheUtil.cache.containsKey("i")) {
				i = (int) CacheUtil.cache.get("i");
			} else {
				i = 1;
			}
			for (TFEventRecord tfEventRecord : records) {
				tfEventRecord.setIndex(i);
				switch (tfEventRecord.getFaceInOut()) {
				case 1:
					tfEventRecord.setInOutType("进口");
					tfEventRecord.setSex("女");
					break;
				default:
					tfEventRecord.setInOutType("出口");
					tfEventRecord.setSex("男");
					break;
				}
				tfEventRecord.setDepartmentName(tfEventRecord.getDepartment().getDepartmentName());
				i++;
			}
			if (records.size() >= 1)
				CacheUtil.cache.put("i", records.get(records.size() - 1).getIndex());
			map.put("records", records);
			map.put("total", records.size());
			map.put("Pages", ((Page<?>) records).getPages());
			map.put("pageNum", ((Page<?>) records).getPageNum());
			map.put("totals", ((Page<?>) records).getTotal());
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			if (!CollectionUtils.isEmpty(records))
				return Result.success("查询成功").withData(map);
			return Result.error("暂无数据!");
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	/**
	 * 统计记录
	 * 
	 * @param request
	 * @param type
	 * @param propertyName
	 * @return
	 */
	@ApiOperation(value = "统计记录信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/countRecordInfo", produces = "application/json;charset=UTF-8")
	public Result countRecordInfo(HttpServletRequest request,
			@ApiParam(value = "条件类型如 当天1,本周2,本月3") @RequestParam(value = "type", required = false) String type,
			@ApiParam("物业名称") @RequestParam(value = "propertyName", required = false) String propertyName) {
		try {
			long start = System.currentTimeMillis();
			Map<String, Object> map = tFEventRecordService.countRecordInfo(type, propertyName);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(map);
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	/**
	 * 当天情况
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "当天情况", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/todaySituation", produces = "application/json;charset=UTF-8")
	public Result todaySituation(HttpServletRequest request, @RequestParam(required = false) String name) {
		try {
			long start = System.currentTimeMillis();
			name = StringUtils.isEmpty(request.getParameter("propertyName")) ? name
					: request.getParameter("propertyName");
			TempEntity map = tFEventRecordService.todayOryesterdayFlux(name);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(map);
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	/**
	 * 当天总情况
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "当天总情况", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/todaySumSituation", produces = "application/json;charset=UTF-8")
	public Result todaySumSituation(HttpServletRequest request, @RequestParam(required = false,value="param") String name) {
		try {
			long start = System.currentTimeMillis();
			name = StringUtils.isEmpty(request.getParameter("propertyName")) ? name
					: request.getParameter("propertyName");
			List<TempEntity> map = tFEventRecordService.findAllSituation(name);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(map);
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	/**
	 * 更新记录
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "更新记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/updateRecord", produces = "application/json;charset=UTF-8")
	public Result updateRecord(HttpServletRequest request, @RequestParam(required = false) String name) {
		try {
			long start = System.currentTimeMillis();
			name = StringUtils.isEmpty(request.getParameter("propertyName")) ? name
					: request.getParameter("propertyName");
			List<TFEventRecord> map = tFEventRecordService
					.listByCondition(TFEventRecord.builder().propertyName(name).build());
			map.stream().forEach(record -> {
				try {
					record.setInOutType(record.getFaceInOut() == 1 ? "进" : "出");
					record.setRecordDateTime(DateUtil.format(record.getTempDate(), "HH:mm:ss"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			});
			newRecord(request);
			CacheUtil.put("newRecord", map);
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(map).withJsonData(map.size());
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "最新记录", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/newRecord", produces = "application/json;charset=UTF-8")
	public Result newRecord(HttpServletRequest request) {
		try {
			long start = System.currentTimeMillis();
			List<TFEventRecord> map = (List<TFEventRecord>) CacheUtil.get("newRecord");		
			TFEventRecord record=map.get(0);
			if(record.getRecordStates()==1) {
				record.setRecordStates(0);
				tFEventRecordService.updateById(record);			
			}
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(map.get(0));
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	/**
	 * 实时进出
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "实时进出", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/currentInOut", produces = "application/json;charset=UTF-8")
	public Result currentInOut(HttpServletRequest request, @RequestParam(required = false) String type,
			@RequestParam(required = false) String name) {
		try {
			long start = System.currentTimeMillis();
			List<TFEmployInfo> inList = tFEventRecordService.selectCurrentTime(
					StringUtils.isEmpty(request.getParameter("type")) ? type : request.getParameter("type"),
					StringUtils.isEmpty(request.getParameter("propertyNo")) ? name
							: request.getParameter("propertyNo"));
			for (TFEmployInfo t : inList) {
				t.setIsPresent(EmptyUtils.isEmpty(t.getIsPresent())?0:t.getIsPresent());
				t.setNum(EmptyUtils.isEmpty(t.getNum())?0:t.getNum());
			}
			long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功").withData(inList);
		} catch (Exception e) {
			log.error("异常==>", e);
			return Result.error("错误!");
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class TempInOut {
		private Integer isInPresent=0;
		private Integer inNum=0;
		private Integer outInPresent=0;
		private Integer outNum=0;
		private String updateTime;
	}

}
