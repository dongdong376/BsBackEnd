package com.jca.datacommon.log;

import lombok.Data;

/**日志记录结果
 * @author Administrator
 * @version 1.0
 * @date 
 */
@Data
public class LogResult {

    private Object[] args;

    private Object result;

    private long time;

    private Exception e;

    public LogResult(Object[] args, Object result, long time) {
        this.args = args;
        this.result = result;
        this.time = time;
    }

    public LogResult(Object[] args, Exception e) {
        this.args = args;
        this.e = e;
    }

    public static LogResult exception(Object[] args, Exception e) {
        return new LogResult(args, e);
    }

}
