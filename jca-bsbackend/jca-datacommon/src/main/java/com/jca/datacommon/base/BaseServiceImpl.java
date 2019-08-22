package com.jca.datacommon.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.github.pagehelper.Page;
import com.jca.datacommon.tool.SpringUtils;
import com.jca.datacommon.web.form.PageForm;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用baseService
 * @author Administrator
 *
 * @param <M>
 * @param <E>
 */
@Slf4j
public class BaseServiceImpl<M extends BaseSqlProvider<E>, E> implements BaseService<E> {

    @Override
    public E save(E e) {
        if (getMapper().insert(e) == 1) {
            return e;
        }
        throw new RuntimeException("保存失败！");
    }

    @Override
    public E updateById(E e) {
        if (getMapper().updateByPrimaryKeySelective(e) == 1) {
            return e;
        }
        throw new RuntimeException("更新失败！");
    }

    @Override
    public Boolean deleteById(Integer id) {
        return getMapper().deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Boolean deleteByCondition(E e) {
        return getMapper().deleteByCriteria(e) != 0;
    }

    @Override
    public Long countByCondition(E e) {
        return getMapper().countByCriteria(e);
    }

    @Override
    public List<E> listAll(String orderBy) {
        return getMapper().selectAll(orderBy);
    }

    @Override
    public List<E> listAll() {
        return getMapper().selectAll("menu_permission_id DESC");
    }

    @Override
    public List<E> listByCondition(E e) {
        return getMapper().selectByCriteria(e);
    }

    @Override
    public E getById(Integer id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public E getByCondition(E e) {
        return getMapper().selectOneByCriteria(e);
    }


    @SuppressWarnings("unchecked")
	private M getMapper() {
        // 获取相应mapper class对象
        Class<?> mapperClazz = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        log.info("class对象"+mapperClazz.getName()+"-"+(M)SpringUtils.getBean(mapperClazz));
        return (M)SpringUtils.getBean(mapperClazz);//强转为所需mapper类
    }

	@Override
	public Page<E> listByCondition(E e, PageForm pageForm) {
		// TODO Auto-generated method stub
		return null;
	}
}
