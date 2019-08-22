package com.jca.datacommon.log.appender.handler;

import com.jca.datacommon.log.appender.LogVO;

/**
 * @author 
 * @version 1.0
 * @date
 */
public class DefaultAppenderHandler implements AppenderHandler {
    @Override
    public void handler(LogVO log) {
        System.out.print(log);
    }
}
