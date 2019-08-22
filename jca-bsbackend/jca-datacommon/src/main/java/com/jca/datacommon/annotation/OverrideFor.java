package com.jca.datacommon.annotation;

import java.lang.annotation.*;

/**
 *  用来覆盖指定元注解中的指定属性（即覆盖注解方法值）
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface OverrideFor {

    String value() default "";

    /**
     * 别名的属性名称(即别名的方法名)
     * 为空则为作用的注解方法元素的方法名
     */
    String attribute() default "";

    /**
     * 声明要覆盖的目标注解的别名属性的注解类型<br/>
     * 即attribute属性的声明类
     */
    Class<? extends Annotation> annotation();
}
