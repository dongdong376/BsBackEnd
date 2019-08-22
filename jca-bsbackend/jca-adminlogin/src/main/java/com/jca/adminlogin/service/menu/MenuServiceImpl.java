package com.jca.adminlogin.service.menu;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jca.databeans.pojo.TFMenu;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.enums.StatusEnum;
import com.jca.datacommon.exception.BusinessException;
import com.jca.datacommon.log.MethodLog;
import com.jca.datadao.TFMenuMapper;
import com.jca.datatool.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单实现类
 * 
 * @author:
 * @date:
 */
@Slf4j
@Service
public class MenuServiceImpl extends BaseServiceImpl<TFMenuMapper, TFMenu> implements MenuService {
	
	@Autowired
	private TFMenuMapper menuMapper;
	
	/**
	 * 获取菜单列表
	 */
	@MethodLog("获取菜单列表")
	@Override	
	public List<TFMenu> getByUserId(Integer userId) {
		return genTreeByMenus(menuMapper.selectByUserId(userId));
	}

	@Override
	//@Cacheable表明所修饰的方法是可以缓存的：当第一次调用这个方法时，它的结果会被缓存下来，在缓存的有效时间内，
	//以后访问这个方法都直接返回缓存结果，不再执行方法中的代码段。//@CachePut会缓存方法的结果，还会执行代码段
	@Cacheable({"myCache"}) // 进行缓存value为字符串数组可以定义多个
	public List<TFMenu> listMenus(TFMenu condition) {
		log.info("没有缓存==>");
		return genTreeByMenus(menuMapper.selectAll("pid ASC, weight DESC"));
	}
	
	@Override
	public TFMenu saveMenu(TFMenu menu) throws BusinessException {
		if (menu.getPid() == null || menu.getPid().equals(0)) {
			menu.setPid(0);
		} else {
			if (getById(menu.getId()) == null) {
				throw new BusinessException("pid不存在！");
			}
		}
		// 默认启用
		menu.setStatus(StatusEnum.ENABLE.getValue());
		try {
			menu.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			menu.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			log.error("异常==>"+e.getMessage());
		}		
		return save(menu);
	}

	@Override
	public void deleteMenu(Integer id) {

	}

	/**
	 * 获取该菜单及其所有子菜单
	 * 
	 * @param id
	 * @return
	 */
	private List<Integer> getChildrenByPid(Integer id) {
		List<Integer> ids = new ArrayList<>();
		ids.add(id);
		Integer pid = id;
		a: for (Iterator<TFMenu> ite = listAll().iterator(); ite.hasNext();) {
			TFMenu menu = ite.next();
			if (menu.getPid().equals(pid)) {
				Integer menuId = menu.getId();
				ids.add(menuId);
				pid = menuId;
				ite.remove();
				break a;
			}
		}
		return ids;
	}

	/**
	 * 生成菜单树
	 * 
	 * @param menus
	 * @return
	 */
	@MethodLog(value="生成菜单树")
	private List<TFMenu> genTreeByMenus(final List<TFMenu> menus) {
		// 获取所有父节点
		List<TFMenu> roots = new ArrayList<>();
		roots=menuMapper.selectByCriteria(TFMenu.builder().pid(0).build());
		// 循环设置子菜单节点
		roots.forEach(r -> {
			setChildren(r, menus);
		});
		return roots;
	}

	/**
	 * 循环设置菜单子节点，使用递归
	 * 
	 * @param parent
	 * @param menus
	 */
	private void setChildren(TFMenu parent, List<TFMenu> menus) {
		List<TFMenu> children = new ArrayList<>();
		for (Iterator<TFMenu> ite = menus.iterator(); ite.hasNext();) {
			TFMenu menu = ite.next();
			if (menu.getPid().equals(parent.getId())) {
				children.add(menu);
				ite.remove();
			}
		}
		// 如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
		if (children.isEmpty()) {
			return;
		}
		parent.setTfMenus(children);
		children.forEach(m -> {
			setChildren(m, menus);
		});
	}

	@Override
	public List<TFMenu> findMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
