package com.jca.systemset.service.role;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.jca.databeans.pojo.TFRole;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFRoleMapper;

@Service
public class RoleServiceImpl extends BaseServiceImpl<TFRoleMapper, TFRole> implements RoleService {
	@Resource
	private TFRoleMapper roleMapper;
	
	@Override
	public TFRole save(TFRole e) {
		Integer result=roleMapper.insert(e);
		if(result>0) {
			e=roleMapper.selectOneByCriteria(e);
		}else {
			e=null;
		}
		return e;
	}

	@Override
	public TFRole updateById(TFRole e) {
		Integer result=roleMapper.updateByPrimaryKeySelective(e);
		if(result>0) {
			e=roleMapper.selectOneByCriteria(e);
		}else {
			e=null;
		}
		return e;
	}

	@Override
	public Boolean deleteById(Integer id) {
		Integer result=roleMapper.deleteByPrimaryKey(id);
		if(result>0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean deleteByCondition(TFRole e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFRole e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFRole> listAll(String orderBy) {		
		return roleMapper.selectAll(orderBy);
	}

	@Override
	public List<TFRole> listAll() {
		return roleMapper.findAllRole();
	}

	@Override
	public List<TFRole> listByCondition(TFRole e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TFRole> listByCondition(TFRole e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFRole getById(Integer id) {		
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public TFRole getByCondition(TFRole e) {
		return roleMapper.selectOneByCriteria(e);
	}

	@Override
	public TFRole getSignleRole(String roleNo) {		
		return roleMapper.getOneRoleByCondition(roleNo);
	}

	@Override
	public Integer deleteRoleInfo(String[] roleId) {
		return roleMapper.deleteRole(roleId);
	}

	@Override
	public List<TFRole> searchRoleByCondition(String roleName) {
		return roleMapper.searchRoleByRoleName(roleName);
	}

	@Override
	public TFRole updateRoleInfo(TFRole role) {
		Integer result=roleMapper.updateRoleInfo(role);
		if(result>0) {
			role=roleMapper.selectOneByCriteria(role);
		}else {
			role=null;
		}
		return role;
	}

	@Override
	public List<TFRole> findRoleList(String[] roleId) {
		return roleMapper.findRoleByCondition(roleId);
	}

}
