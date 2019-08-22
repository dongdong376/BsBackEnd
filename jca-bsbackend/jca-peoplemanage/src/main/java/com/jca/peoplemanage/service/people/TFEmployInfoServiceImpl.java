package com.jca.peoplemanage.service.people;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFOperator;
import com.jca.databeans.vo.AddEmpInfoVo;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.tool.StringUtils;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFDepartmentMapper;
import com.jca.datadao.TFDeviceauthorMapper;
import com.jca.datadao.TFEmployInfoMapper;
import com.jca.datatool.DateUtil;
import com.jca.datatool.EmptyUtils;
import com.jca.datatool.UUIDUtils;
import com.jca.datatool.ValidationToken;
import com.jca.peoplemanage.controller.people.TFEmployInfoController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TFEmployInfoServiceImpl extends BaseServiceImpl<TFEmployInfoMapper, TFEmployInfo>
		implements TFEmployInfoService {

	@Resource
	private TFEmployInfoMapper tFEmployInfoMapper;
	@Resource
	private TFDepartmentMapper TFDepartmentMapper;
	@Resource
	private TFDeviceauthorMapper tFDeviceauthorMapper;	
	@Resource
	private ValidationToken validationToken;

	@Override
	public TFEmployInfo save(TFEmployInfo e) {

		return null;
	}

	@Override
	public TFEmployInfo updateById(TFEmployInfo e) {
		Integer result = tFEmployInfoMapper.updateByPrimaryKeySelective(e);
		if (result > 0)
			return tFEmployInfoMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {

		return null;
	}

	@Override
	public Boolean deleteByCondition(TFEmployInfo e) {

		return null;
	}

	@Override
	public Long countByCondition(TFEmployInfo e) {

		return null;
	}

	@Override
	public List<TFEmployInfo> listAll(String orderBy) {
		return tFEmployInfoMapper.selectAll(orderBy);
	}

	@Override
	public List<TFEmployInfo> listAll() {

		return null;
	}

	@Override
	public List<TFEmployInfo> listByCondition(TFEmployInfo e) {

		return null;
	}

	@Override
	public Page<TFEmployInfo> listByCondition(TFEmployInfo e, PageForm pageForm) {

		return null;
	}

	@Override
	public TFEmployInfo getById(Integer id) {
		return tFEmployInfoMapper.getOneEmpInfo(id);
	}

	@Override
	public TFEmployInfo getByCondition(TFEmployInfo e) {
		return tFEmployInfoMapper.selectOneByCriteria(e);
	}

	@Override
	public List<TFEmployInfo> findAllEmpInfo(TFEmployInfo info, Integer currentPage) throws Exception {
		PageHelper.startPage(currentPage, 10);
		List<TFEmployInfo> employInfos = tFEmployInfoMapper.findAllEmpInfo(info);
		int empI = 1;
		for (TFEmployInfo info2 : employInfos) {
			switch (Integer.valueOf(info2.getPhotoState())) {
			case 0:
				info2.setPhotoState("未上传到相机");
				break;
			case 1:
				info2.setPhotoState("已上传到相机");
				break;
			case 2:
				info2.setPhotoState("图片不合格");
				break;			
			default:
				info2.setPhotoState("没有图片");
				break;
			}
			info2.setIsSubmarineBack(Integer.valueOf(info2.getIsSubmarineBack()) == 0 ? "否" : "是");
			info2.setPhoto(info2.getPhoto().replace("\\\\", "\\"));
			info2.setIndex(empI);
			SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			info2.setBeginDatetime(fromat.format(fromat.parse(info2.getBeginDatetime())));
			info2.setEndDatetime(fromat.format(fromat.parse(info2.getEndDatetime())));
			empI++;
		}
		return employInfos;
	}

	@Override
	public TFEmployInfo addEmployeeInfo(String propertyNo, AddEmpInfoVo empInfoVo, String userId, String propertyName)
			throws Exception {
		TFEmployInfo info = new TFEmployInfo();
		empInfoVo.setDepartmentNo(empInfoVo.getDepartmentNo());
		//图片编码
		String imgData = empInfoVo.getPhoto().substring(empInfoVo.getPhoto().indexOf(",") + 1);
		BeanUtils.copyProperties(empInfoVo, info);
		info = generateEmpNo(info);
		info.setPhoto(propertyNo + "\\\\" + info.getEmployNo() + ".jpg");
		info.setPropertyNo(propertyNo);
		info.setSecretKey(UUIDUtils.generateUUID());
		info.setCreateBy(userId);
		info.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		info.setIsDown(1);// 默认启用
		if (!StringUtils.isEmpty(imgData)) {
			// 将图片保存到本地
			log.info("处理图片==>");
			if (TFEmployInfoController.dealImage(propertyNo, imgData, info.getEmployNo())) {
				info.setPhotoState(String.valueOf(1));
			} else {
				info.setPhotoState(String.valueOf(2));
			}
		} else {
			info.setPhotoState(String.valueOf(0));
		}
		Integer result = tFEmployInfoMapper.insert(info);
		if (result > 0)
			return tFEmployInfoMapper.selectOneByCriteria(info);
		return null;
	}

	/**
	 * 生产员工号
	 * 
	 * @param info
	 * @return
	 */
	private TFEmployInfo generateEmpNo(TFEmployInfo info) {
		Random random = new Random();
		boolean ifHave = true;
		String employNo = String.valueOf(random.nextInt(10000));
		do {
			TFEmployInfo checkEmpInfo = tFEmployInfoMapper
					.selectOneByCriteria(TFEmployInfo.builder().employNo(employNo).build());
			if (EmptyUtils.isEmpty(checkEmpInfo)) {
				info.setEmployNo(employNo);
				ifHave = false;
			} else {
				employNo = String.valueOf(random.nextInt(10000));
				ifHave = true;
			}
		} while (ifHave);
		return info;
	}

	@Override
	public Result removeEmpInfo(String[] ids,String opUserId) {
		List<TFEmployInfo> infos = tFEmployInfoMapper.findDeEmpInfo(ids);
		Map<String, Object> param = new HashMap<String, Object>();
		String[] nos = new String[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			nos[i] = infos.get(i).getEmployNo();
		}
		TFOperator operator=validationToken.getCurrentUser(opUserId);
		param.put("isDown", 0);
		param.put("nos", nos);
		param.put("ids", ids);
		param.put("userId", operator.getOperatorId());
		if (tFEmployInfoMapper.updateEmpInfo(param) > 0) {
			// Integer upAuthor = tFDeviceauthorMapper.updateAuthor(param);
			// if (upAuthor > 0)
			return Result.success("删除成功!");
		}
		return Result.error("删除失败");
	}

	@Override
	public List<TFEmployInfo> findAllEmpInfoByDepOrName(Object... objects) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("propertyNo", objects[2]);
		param.put("depNo", objects[0]);
		param.put("empName", objects[1]);
		return tFEmployInfoMapper.selectEmpInfoByDepOrName(param);
	}

}
