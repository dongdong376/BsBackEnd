package com.jca.facilitymanage.service.author;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFDeviceauthor;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.vo.AddDeviceauthorVo;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.tool.PlaceholderResolver;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFDeviceauthorMapper;
import com.jca.datadao.TFEmployInfoMapper;
import com.jca.datatool.EmptyUtils;

@Service
public class TFDeviceauthorServiceImpl extends BaseServiceImpl<TFDeviceauthorMapper, TFDeviceauthor>
		implements TFDeviceauthorService {

	@Resource
	private TFDeviceauthorMapper tFDeviceauthorMapper;
	@Resource
	private TFEmployInfoMapper tFEmployInfoMapper;

	@Override
	public TFDeviceauthor save(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor updateById(TFDeviceauthor e) {
		Integer result = tFDeviceauthorMapper.updateByPrimaryKeySelective(e);
		if (result > 0)
			return tFDeviceauthorMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> listByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFDeviceauthor> listByCondition(TFDeviceauthor e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDeviceauthor getByCondition(TFDeviceauthor e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDeviceauthor> findAllAuthor(Object... objects) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", objects[1]);
		return tFDeviceauthorMapper.selectDeviceAuthor(param);
	}

	@Override
	public Result insertAuthor(Object... objects) {
		if (EmptyUtils.isEmpty(objects))
			return Result.error();
		AddDeviceauthorVo deviceauthorVo = (AddDeviceauthorVo) objects[0];
		boolean ifsuccess = false;
		for (String empNo : deviceauthorVo.getEmployNos()) {
			TFEmployInfo empInfo = tFEmployInfoMapper
					.selectOneByCriteria(TFEmployInfo.builder().employNo(empNo).build());
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO t_f_deviceauthor (face_sn, employ_no,author,is_download,is_down,card_no) VALUES");
			int i = 0;
			for (String faceNo : deviceauthorVo.getFaceSns()) {
				String values = null;
				if (i < deviceauthorVo.getFaceSns().length - 1) {
					values = " ('${}','${}','${}','${}','${}','${}'),";
				} else {
					values = " ('${}','${}','${}','${}','${}','${}');";
				}
				sb.append(PlaceholderResolver.getDefaultResolver().resolve(values.toString(), faceNo, empNo, "(NULL)",
						"0", "0", empInfo.getCardNo()));
				i++;
			}
			Integer result = tFDeviceauthorMapper.insertAuthor(sb.toString());
			if (result <= 0) {
				ifsuccess = true;
				break;
			}
		}
		if (!ifsuccess) {
			return Result.success();
		}
		return Result.error();
	}

	@Override
	public Integer removeAuthor(String[] ids) {
		Map<String, Object> param = new HashMap<>();
		param.put("isDown", 2);
		param.put("nos", ids);
		return tFDeviceauthorMapper.updateAuthor(param);
	}

}
