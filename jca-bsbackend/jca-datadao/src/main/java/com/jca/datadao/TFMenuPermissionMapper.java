package com.jca.datadao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jca.databeans.pojo.Permission;
import com.jca.databeans.pojo.TFMenuPermission;
import com.jca.datacommon.base.BaseSqlProvider;

/**
 * 
 * @author Administrator
 *
 */
public interface TFMenuPermissionMapper extends BaseSqlProvider<TFMenuPermission> {

	@Select("SELECT \n" + "  menu_permission_id AS menuPermissionId,\n" + "  menu_id AS menuId,\n"
			+ "  if_down AS ifDown,\n" + "  CODE AS CODE,\n" + "  description AS description,\n"
			+ "  STATUS AS STATUS,\n" + "  permission_group_id AS permissionGroupId,\n" + "  url_path urlPath,\n"
			+ "  create_by AS createBy,\n" + "  create_time createTime,\n" + "  update_by AS updateBy,\n"
			+ "  update_time AS updateTime\n" + "FROM\n" + "  t_f_menu_permission  mp\n" + "  WHERE mp.menu_id IN \n"
			+ "  (SELECT DISTINCT \n" + "    (m.id) \n" + "  FROM\n" + "    t_f_menu m\n"
			+ "    INNER JOIN t_f_role_menu_resource frm \n" + "      ON frm.menu_id = m.id \n"
			+ "    INNER JOIN t_f_role fr \n" + "      ON fr.role_no = frm.role_no \n"
			+ "    INNER JOIN t_f_operator fo \n" + "      ON fo.role_no = fr.role_no \n"
			+ "      AND operator_id = #{userId}) \n" + "  AND mp.status = 1 \n" + "ORDER BY mp.menu_id ")
	List<Permission> selectByUserId(@Param("userId")Integer userId);
}
