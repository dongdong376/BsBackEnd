package com.jca.peoplemanage.controller.people;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.vo.AddEmpInfoVo;
import com.jca.databeans.vo.UpdateEmpInfoVo;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.CollectionUtils;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.Base64Utils;
import com.jca.datatool.CacheUtil;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.IsChinese;
import com.jca.datatool.RedisAPI;
import com.jca.datatool.UUIDUtils;
import com.jca.peoplemanage.service.author.TFDeviceauthorService;
import com.jca.peoplemanage.service.people.TFEmployInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api("人员信息")
@Slf4j
@RestController
@RequestMapping("peopleManage/empInfo")
public class TFEmployInfoController {

	@Resource
	private TFEmployInfoService tFEmployInfoService;
	@Resource
	private TFDeviceauthorService tFDeviceauthorService;
	@Resource
	private RedisAPI redisAPI;

	/**
	 * 所有人员信息
	 * 
	 * @param infoParam
	 * @param currentPage
	 * @return
	 */
	@ApiOperation(value = "当前物业下所有人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "当前物业下所有人员信息")
	@RequestMapping(value = "/AllEmpInfo", produces = "application/json;charset=UTF-8")
	public Result AllEmpInfo(@RequestParam(required = false, value = "param") String infoParam,
			@RequestParam(value = "PageNum", required = false, defaultValue = "1") Integer currentPage,
			HttpServletRequest request,
			@RequestParam(value = "property", required = false, defaultValue = "111") String name) {
		try {
			Long start = System.currentTimeMillis();
			TFEmployInfo info = new TFEmployInfo();
			info.setPropertyNo(StringUtils.isEmpty(request.getParameter("propertyNo")) ? name
					: request.getParameter("propertyNo"));
			if (EmptyUtils.isNotEmpty(infoParam)) {
				info.setEmployNo(infoParam);
				info.setEmployName(infoParam);
				info.setDepartmentNo(infoParam);
				info.setCardNo(infoParam);
				info.setBeginDatetime(IsChinese.isChinese(infoParam) ? null : infoParam);
				info.setEndDatetime(IsChinese.isChinese(infoParam) ? null : infoParam);
				if (infoParam.indexOf("-") == -1) {
					info.setIsSubmarineBack((EmptyUtils.isNotEmpty(infoParam)
							? (IsChinese.isChinese(infoParam) && infoParam.length() > 1)
							: EmptyUtils.isEmpty(infoParam)) ? null : infoParam);
					info.setPhotoState((EmptyUtils.isNotEmpty(infoParam)
							? (IsChinese.isChinese(infoParam) && infoParam.length() > 1)
							: EmptyUtils.isEmpty(infoParam)) ? null : infoParam);
				}
			}
			Map<String, Object> map = new HashMap<>();
			List<TFEmployInfo> properties = tFEmployInfoService.findAllEmpInfo(info, currentPage);
			if (CollectionUtils.isEmpty(properties))
				return Result.error("暂无数据!");
			map.put("properties", properties);
			map.put("pageNum", ((Page<?>) properties).getPageNum());
			map.put("total", ((Page<?>) properties).getTotal());
			map.put("totals", properties.size());
			map.put("pages", ((Page<?>) properties).getPages());
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success().withData(map).withJsonData(properties.size());
		} catch (NumberFormatException e) {
			log.error("异常", e);
			return Result.error("参数错误");
		} catch (Exception e1) {
			log.error("异常", e1);
			return Result.error("异常");
		}
	}

	/**
	 * 添加人员信息
	 * 
	 * @param infoParam
	 * @param currentPage
	 * @return
	 */
	@ApiOperation(value = "添加人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "添加人员信息")
	@RequestMapping(value = "/addEmpInfo", produces = "application/json;charset=UTF-8")
	public Result addEmpInfo(@RequestParam("json") String empInfoVoParam, HttpServletRequest request) {
		try {
			String usertoken = request.getHeader("usertoken");
			if (EmptyUtils.isEmpty(usertoken))
				return Result.error("无token!");
			TFOperator operator = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if (EmptyUtils.isEmpty(operator))
				return Result.error("未登陆!");
			Long start = System.currentTimeMillis();
			AddEmpInfoVo empInfoVo = JSON.parseObject(empInfoVoParam, AddEmpInfoVo.class);
			TFEmployInfo info = tFEmployInfoService.addEmployeeInfo(request.getParameter("propertyNo"), empInfoVo,
					operator.getOperatorId().toString(), request.getParameter("propertyName"));
			// 缓存员号
			CacheUtil.cache.put("employNo-" + info.getEmployNo(), info.getEmployNo());
			if (EmptyUtils.isEmpty(info))
				return Result.error("添加失败!");
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("添加成功!").withData(info);
		} catch (NumberFormatException e) {
			log.error("格式有误!", e.getMessage());
			return Result.error("异常");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("异常!", e1.getMessage());
			return Result.error("异常");
		}
	}

	/**
	 * 查看人员信息
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "查看人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查看人员信息")
	@RequestMapping(value = "/getEmpInfo", produces = "application/json;charset=UTF-8")
	public Result getEmpInfo(@RequestParam(value = "id", required = true) Integer id, HttpServletRequest request) {
		try {
			Long start = System.currentTimeMillis();
			TFEmployInfo info = tFEmployInfoService.getById(id);
			CacheUtil.cache.put("employNo" + info.getEmployId(), info.getEmployNo());
			info.setIsSubmarineBack(Integer.valueOf(info.getIsSubmarineBack()) == 0 ? "否" : "是");
			SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			info.setBeginDatetime(fromat.format(fromat.parse(info.getBeginDatetime())));
			info.setEndDatetime(fromat.format(fromat.parse(info.getEndDatetime())));
			info.setSexCN(info.getSex() == 0 ? "女" : "男");
			info.setPhoto(info.getPhoto().replace("\\\\", "\\"));
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功!").withData(info);
		} catch (Exception e1) {
			log.error("异常!", e1.getMessage());
			return Result.error("异常");
		}
	}

	/**
	 * 删除人员信息
	 * 
	 * @param idsParam
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "删除人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "删除人员信息")
	@RequestMapping(value = "/removeEmpInfo", produces = "application/json;charset=UTF-8")
	public Result removeEmpInfo(@RequestParam(value = "ids", required = true) String idsParam,
			HttpServletRequest request) {
		try {
			Long start = System.currentTimeMillis();
			String ids[] = idsParam.split(",");
			Result resul = tFEmployInfoService.removeEmpInfo(ids, request.getHeader("usertoken"));
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return resul;
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("异常!", e1.getMessage());
			return Result.error("异常");
		}
	}

	/**
	 * 修改人员信息
	 * 
	 * @param empInfoVoParam
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "修改人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "修改人员信息")
	@RequestMapping(value = "/updateEmpInfo", produces = "application/json;charset=UTF-8")
	public Result updateEmpInfo(@RequestParam("json") String empInfoVoParam, HttpServletRequest request) {
		try {
			Long start = System.currentTimeMillis();
			String usertoken = request.getHeader("usertoken");
			if (EmptyUtils.isEmpty(usertoken))
				return Result.error("无token!");
			TFOperator operator = JSON.parseObject(redisAPI.get(usertoken), TFOperator.class);
			if (EmptyUtils.isEmpty(operator))
				return Result.error("未登陆!");
			UpdateEmpInfoVo empInfoVo = JSON.parseObject(empInfoVoParam, UpdateEmpInfoVo.class);
			TFEmployInfo info = new TFEmployInfo();
			BeanUtils.copyProperties(empInfoVo, info);
			info.setUpdateBy(operator.getOperatorId().toString());
			info.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			String imgData = info.getPhoto().substring(info.getPhoto().indexOf(",") + 1);
			// 修改查看时的工号
			String empNo = CacheUtil.cache.get("employNo" + info.getEmployId()).toString();
			info.setPhoto(request.getParameter("propertyNo") + "\\\\" + empNo + ".jpg");
			if (!StringUtils.isEmpty(imgData)) {
				if (dealImage(request.getParameter("propertyNo"), imgData, empNo)) {
					info.setPhotoState(String.valueOf(1));
				} else {
					info.setPhotoState(String.valueOf(2));
				}
				TFEmployInfo info2 = tFEmployInfoService
						.getByCondition(TFEmployInfo.builder().photo(info.getPhoto()).build());
				// 修改权限认证表
				if (EmptyUtils.isEmpty(info2)) {
					String[] ids = new String[1];
					ids[0] = empNo;
					tFDeviceauthorService.updateAuthor(ids);
				}
			} else {
				info.setPhotoState(String.valueOf(0));
			}
			TFEmployInfo resultInfo = tFEmployInfoService.updateById(info);
			if (EmptyUtils.isEmpty(resultInfo))
				return Result.error("修改失败!");
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("修改成功!").withData(info);
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("异常!", e1.getMessage());
			return Result.error("异常");
		}
	}

	@ApiOperation(value = "查询人员信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "查询人员信息")
	@RequestMapping(value = "/searchEmpInfo", produces = "application/json;charset=UTF-8")
	public Result searchEmpInfo(@ApiParam("部门No")@RequestParam(value = "depNo", required = false) String depNo,
			HttpServletRequest request,@ApiParam("人员No") @RequestParam(value = "empName", required = false) String empName,
			@ApiParam("物业No")@RequestParam(value = "name", required = false) String name) {
		try {
			Long start = System.currentTimeMillis();
			if(!StringUtils.isEmpty(request.getParameter("propertyNo")))
				name=request.getParameter("propertyNo");
			List<TFEmployInfo> result = tFEmployInfoService.findAllEmpInfoByDepOrName(depNo, empName, name);
			Long end = System.currentTimeMillis();
			log.info("耗时==>" + (end - start));
			return Result.success("查询成功!").withData(result).withJsonData(result.size());
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("异常!", e1.getMessage());
			return Result.error("异常");
		}
	}

	/**
	 * 处理图片
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	public static boolean dealImage(String propertyNo, String imgData, String empNo) {
		String path = "D:\\jpeg\\user\\" + propertyNo;
		try {
			// 创建文件
			File dir = new File(path);
			log.info("目录是否存在==>" + dir.exists());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String img = empNo + ".jpg";// zhao.jpg
			log.info("图片==>" + img);
			path += "\\" + img;
			Base64Utils.convertBase64DataToImage(imgData, path);
			;
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
