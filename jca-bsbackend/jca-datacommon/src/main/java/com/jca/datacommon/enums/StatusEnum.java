package com.jca.datacommon.enums;

/**
 * 状态枚举
 * @author Administrator
 *
 */
public enum StatusEnum implements ValueEnum {
    /**
     * 禁用状态
     */
    DISABLE(0),

    /**
     * 启用状态
     */
    ENABLE(1);

    /**
     * 状态值
     */
    private Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return this.value;
    }
}


