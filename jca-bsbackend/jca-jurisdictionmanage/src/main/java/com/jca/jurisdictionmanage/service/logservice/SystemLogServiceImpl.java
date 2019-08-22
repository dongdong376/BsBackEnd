package com.jca.jurisdictionmanage.service.logservice;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jca.datacommon.SystemLog;
import com.jca.datacommon.annotation.DataSource;
import com.jca.datadao.SystemLogMapper;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Resource
    private SystemLogMapper systemLogMapper;
    
    @Override
    public Integer deleteSystemLog(String id) {        
              return systemLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    //@CachePut(value="myCache")
    //@CacheEvict(value="myCache",allEntries=true,beforeInvocation=true)
    @CacheEvict(value="myCache",key="0",beforeInvocation=true)//进行缓存
    @DataSource(DataSource.log)
    public Integer insert(SystemLog record) {     
              return systemLogMapper.insert(record);
    }

    @Override
    @Cacheable(value="myCache",key="#id")
    public SystemLog findSystemLog(String id) {        
             return systemLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateSystemLog(SystemLog record) {        
             return systemLogMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public Integer insertTest(SystemLog record) {        
           return systemLogMapper.insert(record);
    }

    @Override
    @Cacheable(value="myCache",key="0")
    public Long count() {
           Long num = systemLogMapper.count();
           return num;
    }

}