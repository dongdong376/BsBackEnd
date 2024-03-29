package com.jca.datacommon.enums;

import com.jca.datacommon.enums.NameValueEnum;

/**
 * 结果枚举类
 * @author Administrator
 *
 */
public enum ResultEnum implements NameValueEnum {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功！"),

    /**
     * 失败
     */
    ERROR(400, "操作失败！"),

    /**
     * 参数错误
     */
    PARAM_ERROR(405, "参数错误！"),

    /**
     * 资源未找到
     */
    NO_FOUND(404, "未找到对应的资源！"),

    /**
     * 服务器异常
     */
    SERVER_ERROR(500, "服务器异常！"),

    /**
     * 无权限
     */
    NO_PERMISSION(501, "没有权限！"),
    
    /**
     * 存在
     */
	EXIST(400,"已经存在"),
	
	 /**
     * 存在
     */
	NOT_EXIST(400,"不存在");
	
    /**
     * 结果操作码
     */
    private Integer code;
    /**
     * 结果消息
     */
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getName() {
        return this.msg;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
