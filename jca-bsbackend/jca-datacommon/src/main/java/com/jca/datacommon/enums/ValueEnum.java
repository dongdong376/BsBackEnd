package com.jca.datacommon.enums;

/**
 * 最简单的枚举类，即只含value的枚举（实现此接口可使用{@link mayfly.common.util.EnumUtils}中的方法）
 * @author Administrator
 *
 */
public interface ValueEnum {
    /**
     * 获取枚举值
     * @return  枚举值
     */
    Integer getValue();
}
