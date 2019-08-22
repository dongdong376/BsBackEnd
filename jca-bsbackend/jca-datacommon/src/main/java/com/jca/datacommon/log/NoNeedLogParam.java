package com.jca.datacommon.log;

import java.lang.annotation.*;

/**
 * @author hml
 * @version 1.0
 * @description: 如果不需要记录日志的方法参数需要有该注解
 * @date 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedLogParam {
}
