package com.jca.datacommon.aop.validator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jca.datacommon.annotation.Valid;
import com.jca.datacommon.annotation.tool.AnnotationUtils;
import com.jca.datacommon.exception.ParamValidErrorException;
import com.jca.datacommon.validation.handler.ValidationHandler;

/**
 * aop参数验证器
 * 懒汉模式
 * @author Administrator
 *
 */
public class AopParamValidator {

    /**
     * 带有@Valid参数的索引位置, key:方法全路径  value:索引列表（即参数可以含有多个@Valid注解）
     */
    private static Map<Method, List<Integer>> indexCache = new ConcurrentHashMap<>(64);


    private AopParamValidator(){}

    private static AopParamValidator validator = new AopParamValidator();

    public static AopParamValidator getInstance() {
        return validator;
    }


    public void validate(Method method, Object[] args) throws ParamValidErrorException {
        List<Integer> idx = indexCache.computeIfAbsent(method, key -> {
            List<Integer> index = null;
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                if(AnnotationUtils.isAnnotationPresent(params[i], Valid.class)) {
                    if (index == null) {
                        index = new ArrayList<>();
                    }
                    index.add(i);
                }
            }
            if (index == null) {
                index = Collections.emptyList();
            }
            return index;
        });
        // 没有需要校验的参数索引，直接返回
        if (idx.isEmpty()) {
            return;
        }
        for (Integer i : idx) {
            ValidationHandler.getInstance().validate(args[i]);
        }
    }

}
