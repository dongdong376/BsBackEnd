package com.jca.datacommon.annotation.validator;;

/**
 * 校验结果
 * @author Administrator
 *
 */
public class ValidResult {

    private String message;

    private boolean right;

    private ValidResult(){}
    private ValidResult(boolean right, String message) {
        this.right = right;
        this.message = message;
    }

    /**
     * @return  返回正确的校验结果
     */
    public static ValidResult right() {
        return new ValidResult(true, null);
    }

    /**
     * @param message  错误消息
     * @return  返回错误的校验结果
     */
    public static ValidResult error(String message) {
        return new ValidResult(false, message);
    }


    public String getMessage() {
        return message;
    }

    public boolean isRight() {
        return right;
    }
}
