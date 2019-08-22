package com.jca.systemset.controller.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jca.databeans.pojo.Menu;
import com.jca.databeans.pojo.TFMenu;
import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.tool.Result;
import com.jca.systemset.service.menu.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "菜单管理")
//@MethodLog(value = "菜单管理")
@RestController
@RequestMapping("/menumanage/menu")
public class MenuController {
	@Resource
	private MenuService menuService;

	@ApiOperation(value = "获取所有菜单信息", protocols = "HTTP", produces = "application/json;charset=UTF-8")
	//@MethodLog(value = "获取所有菜单信息")
	@RequestMapping(value = "/findAllMenu", produces = "application/json;charset=UTF-8")
	public Result findAllMenu(@RequestParam(required = false) @ApiParam(value = "指定orderBy排序语句") String orderBy) {
		long start = System.currentTimeMillis();
		// 所有菜单
		List<TFMenu> tfMenus = menuService.findMenu();
		log.info("总菜单数量==" + tfMenus.size());
		List<TFMenu> manuTree = new ArrayList<>();
		manuTree = this.generatorTree(tfMenus);
		long end = System.currentTimeMillis();
		log.info("所耗时间==>" + (end - start));
		return Result.success().withData(manuTree);
	}

	/**
	 * 生成菜单树
	 * 
	 * @param tfMenus
	 * @return
	 */
	public List generatorTree(List<TFMenu> tfMenus) {
		// 父菜单列表
		List<TFMenu> parentList = new ArrayList<>();
		for (TFMenu aMenu : tfMenus) {
			if (aMenu.getPid() == 0) {
				parentList.add(aMenu);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (TFMenu pMenu : parentList) {
			List<TFMenu> cmenus = new ArrayList<>();
			for (int i = 0; i < tfMenus.size(); i++) {
				if (pMenu.getId() == tfMenus.get(i).getPid()) {// 判断父子关系
					cmenus.add(tfMenus.get(i));// 添加子集合
					pMenu.setTfMenus(cmenus);
				}
			}
		}

		return parentList;
	}

}
