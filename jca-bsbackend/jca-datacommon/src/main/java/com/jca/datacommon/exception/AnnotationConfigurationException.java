package com.jca.datacommon.exception;

/**
 *  注解配置异常
 * @author Administrator
 *
 */
public class AnnotationConfigurationException extends RuntimeException {

    public AnnotationConfigurationException(String msg) {
        super(msg);
    }

    public AnnotationConfigurationException(String msg, Exception e) {
        super(msg, e);
    }
}
