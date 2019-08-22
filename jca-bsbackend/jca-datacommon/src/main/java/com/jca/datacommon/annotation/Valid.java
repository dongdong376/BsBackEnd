package com.jca.datacommon.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 
 * @author Administrator
 *
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface Valid {
}
