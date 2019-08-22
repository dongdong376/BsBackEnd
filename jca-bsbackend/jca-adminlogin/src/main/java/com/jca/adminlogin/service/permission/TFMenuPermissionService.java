package com.jca.adminlogin.service.permission;

import java.util.List;

import com.jca.databeans.pojo.TFMenuPermission;
import com.jca.databeans.vo.LoginSuccessVO;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.web.UriPattern;

/**
 * 权限服务
 * @author: 
 * @date:
 */
public interface TFMenuPermissionService extends BaseService<TFMenuPermission> {
    /**
     * 保存id以及对应的权限
     * @param id  userId
     * @return   token
     */
    LoginSuccessVO saveIdAndPermission(Integer operator);

    /**
     * 根据token获取用户id
     * @param token
     * @return  userId
     */
    Integer getIdByToken(String token);

    /**
     * 根据token获取用户api路径权限
     * @param token
     * @return
     */
    List<UriPattern> getUriPermissionByToken(String token);

    /**
     * 保存权限
     * @param permission
     * @return
     */
    TFMenuPermission savePermission(TFMenuPermission permission);

    TFMenuPermission updatePermission(TFMenuPermission permission);

    /**
     * 改变redis中权限状态code
     * @param id 权限code
     * @param status
     * @return
     */
    TFMenuPermission changeStatus(Integer id, Integer status);

    /**
     * 删除权限，并且删除角色关联的该权限记录
     * @param id
     * @return
     */
    Boolean deletePermission(Integer id);

}
