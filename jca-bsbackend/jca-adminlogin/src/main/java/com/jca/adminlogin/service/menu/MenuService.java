package com.jca.adminlogin.service.menu;



import java.util.List;

import com.jca.databeans.pojo.TFMenu;
import com.jca.datacommon.base.BaseService;
import com.jca.datacommon.exception.BusinessException;

/**
 * @Description: 菜单服务接口
 * @author: hml
 * @date: 
 */
public interface MenuService extends BaseService<TFMenu> {

    /**
     * 根据用户id获取用户的所有菜单权限
     * @param userId 用户id
     * @return   菜单列表
     */
    List<TFMenu> getByUserId(Integer userId);

    /**
     * 获取所有菜单
     * @return
     */
    List<TFMenu> listMenus(TFMenu condition);

    /**
     * 删除指定菜单，如果是有子节点，也删除
     * @param id
     * @return
     */
    void deleteMenu(Integer id);

    /**
     * 保存菜单
     * @param menu
   @return
     * @throws BusinessException
     */
    TFMenu saveMenu(TFMenu menu) throws BusinessException;
    
    List<TFMenu> findMenu();
}
