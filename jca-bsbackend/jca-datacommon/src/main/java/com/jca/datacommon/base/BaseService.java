package com.jca.datacommon.base;


import java.util.List;

import com.github.pagehelper.Page;
import com.jca.datacommon.web.form.PageForm;

/**通用baseservice服务使用泛型
 * @author Administrator
 * @version 1.0
 * @date 
 */
public interface BaseService< E> {

    /**
     * 保存实体
     * @param e
     * @return
     */
    E save(E e);

    /**
     * 根据id更新实体
     * @param e
     * @return
     */
    E updateById(E e);

    /**
     * 删除指定id的实体
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 根据条件删除
     * @param e
     * @return
     */
    Boolean deleteByCondition(E e);

    /**
     * 根据条件统计实体数
     * @param e
     * @return
     */
    Long countByCondition(E e);


    /**
     * 获取所有数据
     * @param orderBy  排序条件
     * @return
     */
    List<E> listAll(String orderBy);

    List<E> listAll();

    /**
     * 根据实体条件查询实体列表
     * @param e
     * @return
     */
    List<E> listByCondition(E e);

    Page<E> listByCondition(E e, PageForm pageForm);

    E getById(Integer id);

    /**
     * 根据条件获取单个对象
     * @param e
     * @return
     */
    E getByCondition(E e);
}
