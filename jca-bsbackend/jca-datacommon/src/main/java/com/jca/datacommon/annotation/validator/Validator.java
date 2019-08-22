package com.jca.datacommon.annotation.validator;;

/**
 * 校验器接口
 * @author Administrator
 *
 * @param <A>
 * @param <R>
 */
public interface Validator<A, R> {

    /**
     * 校验规则
     * @param annotation 校验规则注解
     * @param value  校验值对象
     * @return  是否符合校验规则
     */
    ValidResult validation(A annotation, Value<R> value);
}
