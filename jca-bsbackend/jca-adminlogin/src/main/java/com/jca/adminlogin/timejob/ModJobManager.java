package com.jca.adminlogin.timejob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class ModJobManager implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModJobManager.class);

    public void init() {
        LOGGER.info("初始化job!======>");
        //通过spring上下文获取quartzManager，SpringContextHolder 类见下一个文件
        //QuartzManager quartzManager = (QuartzManager) SpringContextHolder.getBean("quartzManager");
        //quartzManager.addJob("testJob", "testJob", "testJob1", "testJob1", TestJob.class, "0 0/2 * * * ?");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        init();
    }

}