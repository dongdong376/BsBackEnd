package com.jca.datacommon.exception;

/**
 * 校验参数错误异常
 * @author Administrator
 *
 */
public class ParamValidErrorException extends Exception {

    public ParamValidErrorException(String msg) {
        super(msg);
    }
}
