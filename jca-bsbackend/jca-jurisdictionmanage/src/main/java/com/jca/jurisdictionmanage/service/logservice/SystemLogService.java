package com.jca.jurisdictionmanage.service.logservice;

import com.jca.datacommon.SystemLog;

public interface SystemLogService {

    Integer deleteSystemLog(String id);

    Integer insert(SystemLog record);
    
    Integer insertTest(SystemLog record);

    SystemLog findSystemLog(String id);
    
    Integer updateSystemLog(SystemLog record);
    
    Long count();
}