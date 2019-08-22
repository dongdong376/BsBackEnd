package com.jca.datacommon.moredatasource;

/**
 * 定义一个可以设置当前线程的变量的工具类，用于设置对应的数据源名称
 * @author Administrator
 *
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	/**
	 * @Description: 设置数据源类型 @param dataSourceType 数据库类型 @return void @throws
	 */
	public static void setDataSourceType(String dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	/**
	 * @Description: 获取数据源类型 @param @return String @throws
	 */
	public static String getDataSourceType() {
		return contextHolder.get();
	}

	/**
	 * @Description: 清除数据源类型 @param @return void @throws
	 */
	public static void clearDataSourceType() {
		contextHolder.remove();
	}
}