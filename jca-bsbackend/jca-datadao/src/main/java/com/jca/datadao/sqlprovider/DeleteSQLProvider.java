package com.jca.datadao.sqlprovider;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;

import com.jca.datacommon.TableInfo;
import com.jca.datacommon.annotation.NoColumn;
import com.jca.datacommon.annotation.Primary;
import com.jca.datacommon.base.BaseSqlProvider;
import com.jca.datacommon.tool.ReflectionUtils;
import com.jca.datacommon.tool.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 有关删除
 * @author Administrator
 *
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class DeleteSQLProvider {
	/**
	 * 表前缀
	 */
	private static final String TABLE_PREFIX = "t_";

	/**
	 * 主键名
	 */
	private static final String DEFAULT_PRIMARY_KEY = "id";
	/**
	 * key:interface class value:tableInfo 缓存表
	 */
	private static Map<Class, TableInfo> tableCache = new ConcurrentHashMap<Class, TableInfo>(256);

	public String deleteRoleByArray(@Param(value = "paramIds") String paramIds[], ProviderContext context) {
		TableInfo table = tableInfo(context);
		StringBuilder sb = new StringBuilder("delete from " + table.getTableName() + " where "+table.getPrimaryKeyColumn()+" in(");
		for (int i = 0; i < paramIds.length; i++) {
			if (i == paramIds.length - 1) {
				sb.append(paramIds[i] + ")");
			} else {
				sb.append(paramIds[i] + ",");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取表信息结构
	 * 
	 * @param context
	 * @return
	 */
	protected TableInfo tableInfo(ProviderContext context) {
		TableInfo info = tableCache.get(context.getMapperType());
		if (info != null) {
			return info;
		}
		Class<?> entityClass = entityType(context);
		// 获取不含有@NoColumn注解的fields
		Field[] fields = excludeNoColumnField(ReflectionUtils.getFields(entityClass));
		info = TableInfo.entityClass(entityClass).fields(fields).tableName(tableName(entityClass))
				.primaryKeyColumn(primaryKeyColumn(fields)).columns(columns(fields))
				.selectColumns(selectColumns(fields)).build();
		tableCache.put(context.getMapperType(), info);
		return info;
	}

	/**
	 * 获取BaseSqlProvider接口中的泛型类型
	 * 
	 * @param context
	 * @return
	 */
	protected Class<?> entityType(ProviderContext context) {
		return Stream.of(context.getMapperType().getGenericInterfaces()).filter(ParameterizedType.class::isInstance)
				.map(ParameterizedType.class::cast).filter(type -> type.getRawType() == BaseSqlProvider.class)
				.findFirst().map(type -> type.getActualTypeArguments()[0]).filter(Class.class::isInstance)
				.map(Class.class::cast).orElseThrow(() -> new IllegalStateException(
						"未找到BaseSqlProvider的泛型类 " + context.getMapperType().getName() + "."));
	}

	protected String tableName(Class<?> entityType) {
		return StringUtils.camel2Underscore(entityType.getSimpleName());
	}

	/**
	 * 过滤含有@NoColumn注解的field
	 * 
	 * @param totalField
	 *            entityClass所有的字段
	 * @return 不包含@NoColumn注解的fields
	 */
	protected Field[] excludeNoColumnField(Field[] totalField) {
		return Stream.of(totalField)
				// 过滤含有@NoColumn注解的field
				.filter(field -> !field.isAnnotationPresent(NoColumn.class)).toArray(Field[]::new);
	}

	/**
	 * 获取查询对应的字段 (不包含pojo中含有@NoColumn主键的属性)
	 *
	 * @param fields
	 *            p
	 * @return
	 */
	protected String[] selectColumns(Field[] fields) {
		return Stream.of(fields).map(this::selectColumnName).toArray(String[]::new);
	}

	/**
	 * 获取所有pojo所有属性对应的数据库字段 (不包含pojo中含有@NoColumn主键的属性)
	 *
	 * @param fields
	 *            entityClass所有fields
	 * @return
	 */
	protected String[] columns(Field[] fields) {
		return Stream.of(fields).map(this::columnName).toArray(String[]::new);
	}

	/**
	 * 如果fields中含有@Primary的字段，则返回该字段名为主键，否则默认'id'为主键名
	 * 
	 * @param fields
	 *            entityClass所有fields
	 * @return 主键column(驼峰转为下划线)
	 */
	protected String primaryKeyColumn(Field[] fields) {
		return Stream.of(fields).filter(field -> field.isAnnotationPresent(Primary.class)).findFirst() // 返回第一个primaryKey的field
				.map(this::columnName).orElse(DEFAULT_PRIMARY_KEY);
	}

	/**
	 * 获取单个属性对应的数据库字段（带有下划线字段将其转换为"字段 AS pojo属性名"形式）
	 *
	 * @param field
	 * @return
	 */
	protected String selectColumnName(Field field) {
		String camel = StringUtils.camel2Underscore(field.getName());
		return camel.contains("_") ? camel + " AS " + field.getName() : camel;
	}

	/**
	 * 获取单个属性对应的数据库字段
	 *
	 * @param field
	 *            entityClass中的field
	 * @return
	 */
	protected String columnName(Field field) {
		return StringUtils.camel2Underscore(field.getName());
	}

	protected String bindParameter(Field field) {
		return "#{" + field.getName() + "}";
	}

	protected Object value(Object bean, Field field) {
		try {
			field.setAccessible(true);
			return field.get(bean);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} finally {
			field.setAccessible(false);
		}
	}
}
