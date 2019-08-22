package com.jca.datacommon.log.appender;

import lombok.Data;

/**日志视图对象
 * @author 
 * @date 
 */
@Data
public class LogVO {

    private String project;

    private String ip;

    private String msg;

    public LogVO(){}
    public LogVO(String project, String ip, String msg) {
        this.project = project;
        this.ip = ip;
        this.msg = msg;
    }
   
    @Override
    public String toString() {
        return super.toString();
    }
}
