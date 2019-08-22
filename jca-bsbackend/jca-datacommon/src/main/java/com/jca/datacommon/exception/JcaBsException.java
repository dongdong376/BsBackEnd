package com.jca.datacommon.exception;


/**
 * jca异常
 * @author Administrator
 *
 */
public class JcaBsException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//异常码
    private String exceptionCode;
    //异常信息
    private String message;

    public JcaBsException(String exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }

    public JcaBsException(String exceptionCode, String message) {
        super();
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
