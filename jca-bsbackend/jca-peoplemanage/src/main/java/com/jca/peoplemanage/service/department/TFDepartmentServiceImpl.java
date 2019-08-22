package com.jca.peoplemanage.service.department;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFDepartment;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFDepartmentMapper;
import com.jca.datatool.EmptyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TFDepartmentServiceImpl extends BaseServiceImpl<TFDepartmentMapper, TFDepartment>
		implements TFDepartmentService {

	@Resource
	private TFDepartmentMapper tFDepartmentMapper;
	
	//@MethodLog(value="添加部门")
	@Override
	public TFDepartment save(TFDepartment e) {
		Integer result = tFDepartmentMapper.insert(e);
		if (result > 0)
			return tFDepartmentMapper.selectOneByCriteria(e);
		return null;
	}
	//@MethodLog("修改部门")
	@Override
	public TFDepartment updateById(TFDepartment e) {
		Integer result = tFDepartmentMapper.updateByPrimaryKeySelective(e);
		if (result > 0)
			return tFDepartmentMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFDepartment e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFDepartment e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDepartment> listAll(String orderBy) {		
		return tFDepartmentMapper.findDepInfo(null,null);
	}

	@Override
	public List<TFDepartment> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFDepartment> listByCondition(TFDepartment e) {
		return tFDepartmentMapper.findDepInfo(null, e.getPropertyNo());
	}

	@Override
	public Page<TFDepartment> listByCondition(TFDepartment e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFDepartment getById(Integer id) {
		// TODO Auto-generated method stub
		return tFDepartmentMapper.selectByPrimaryKey(id);
	}

	@Override
	public TFDepartment getByCondition(TFDepartment e) {
		// TODO Auto-generated method stub
		return tFDepartmentMapper.selectOneByCriteria(e);
	}
	//@MethodLog(value="移除部门")
	@Override
	public Result removeDepInfo(String[] ids) throws Exception {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		List<TFDepartment> departments = tFDepartmentMapper.findDepInfo(ids,null);
		for (int i = 0; i < departments.size(); i++) {
			TFDepartment d = departments.get(i);
			d.setMemberNum(d.getInfos().size());
			if (d.getMemberNum() >= 1) {
				ids[i] = null;
				sb.append(d.getDepartmentName());
			} else {
				sb1.append(d.getDepartmentName());
				departments.remove(i);
			}
		}
		if(EmptyUtils.isEmpty(ids))
			return Result.error("无法删除!");
		Integer result = tFDepartmentMapper.removeDepInfo(ids);
		if (result > 0)
			return Result.success(EmptyUtils.isEmpty(sb) ? (EmptyUtils.isEmpty(sb1) ? "" : "删除成功!") : "删除成功，部分部门无法删除!");
		return Result.error(EmptyUtils.isEmpty(sb) ? (EmptyUtils.isEmpty(sb1) ? "" : "删除失败!") : "删除失败，部分部门无法删除!");
	}
		
}
