package com.jca.datacommon.enums;

/**
 * 方法枚举
 * @author Administrator
 *
 */
public enum MethodEnum implements ValueEnum {

    /**
     * get请求方法
     */
    GET(1),

    /**
     * post请求方法
     */
    POST(2),

    /**
     * put请求方法
     */
    PUT(3),

    /**
     * delete请求方法
     */
    DELETE(4);


    private Integer value;

    MethodEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
