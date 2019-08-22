package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jca.databeans.pojo.TFMenu;
import com.jca.datacommon.base.BaseSqlProvider;

/**
 * 菜单接口
 * 
 * @author Administrator
 *
 */
public interface TFMenuMapper extends BaseSqlProvider<TFMenu> {
	/**
	 * 登录进来所可以获取的菜单可以显示
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT \r\n" + "  id AS id,\r\n" + "  pid AS pid,\r\n" + "  m.code AS code,\r\n" + "  m.name AS NAME,\r\n"
			+ "  icon AS icon,page_url as pageUrl,\r\n" + "  path AS path,\r\n" + "  weight AS weight,\r\n" + "  m.status AS STATUS,\r\n"
			+ "  create_time AS createTime,\r\n" + "  update_time AS updateTime \r\n" + "FROM\r\n" + "  t_f_menu m \r\n"
			+ "WHERE m.id IN \r\n" + "  (SELECT DISTINCT \r\n" + "    (mp.id) \r\n" + "  FROM\r\n"
			+ "    t_f_menu mp \r\n" + "    INNER JOIN t_f_role_menu_resource frm \r\n"
			+ "      ON frm.menu_id = mp.id \r\n" + "    INNER JOIN t_f_role fr \r\n"
			+ "      ON fr.role_no = frm.role_no \r\n" + "    INNER JOIN t_f_operator fo \r\n"
			+ "      ON fo.role_no = fr.role_no \r\n" + "      AND operator_id = #{userId}) \r\n" + "  AND m.status = 1 \r\n"
			+ "GROUP BY m.pid,m.id\r\n" + 
			"  ORDER BY m.pid ASC,m.weight ASC ")
	List<TFMenu> selectByUserId(@Param("userId")Integer userId);

	/**
	 * 查询所有菜单
	 * 
	 * @return
	 */
	@Select("SELECT * FROM t_f_menu m WHERE m.status=1 order by m.pid")
	List<TFMenu> findMenuList();
}
