package com.jca.datacommon.log.appender.handler;

import com.jca.datacommon.log.appender.LogVO;

/**
 * @author
 * @version 1.0
 * @date 
 */
@FunctionalInterface
public interface AppenderHandler {

    /**
     * 日志对象处理器
     * @param log
     */
    void handler(LogVO log);
}
