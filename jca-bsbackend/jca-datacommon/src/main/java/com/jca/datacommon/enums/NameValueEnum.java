package com.jca.datacommon.enums;

/**
 * 带有枚举值以及枚举说明的枚举接口(可使用{@link mayfly.common.util.EnumUtils}中的方法)
 * @author Administrator
 *
 */
public interface NameValueEnum extends ValueEnum {
    /**
     * 获取枚举名称
     * @return
     */
    String getName();
}
