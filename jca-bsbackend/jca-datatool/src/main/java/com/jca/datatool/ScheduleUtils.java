package com.jca.datatool;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;//工具并发

import lombok.extern.slf4j.Slf4j;

/**
 * 任务调度工具类
 * @author Administrator
 *
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class ScheduleUtils {

    public static final int corePoolSize = 1;

    /**
     *  定时任务线程池
     */
    private static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(corePoolSize);

    /**
     * 存定时任务结果
     */
    
	private static Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    static {
    	log.info("==>定时任务开始");
        //定期检查map中是否有已经执行完成的，有则移除
        scheduleAtFixedRate("removeCompletedFuture", () -> {//第二参数为线程去执行任务
            scheduledFutureMap.forEach((key, value) -> {//java8二元消费
                if (value.isDone()) {
                	log.info("==>定时任务开始：移除已经完成的任务");
                    removeFuture(key);
                }
            });
        }, 0, 1, TimeUnit.SECONDS);
    }


    /**
     * 倒计时执行
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void schedule(String id, Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledFutureMap.put(id, schedule.schedule(runnable, time, timeUnit));
    }

    /**
     * 周期性定时执行
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void scheduleAtFixedRate(String id, Runnable runnable, long initialDelay, long time, TimeUnit timeUnit) {
    	log.info("==>定时任务放入结果集中");
        scheduledFutureMap.put(id, schedule.scheduleAtFixedRate(runnable, initialDelay, time, timeUnit));
    }
    
    /**
     * 移除已经做过的任务
     * @param id
     */
    public static void removeFuture(String id) {
        Optional.ofNullable(scheduledFutureMap.get(id)).ifPresent(x -> {
            scheduledFutureMap.remove(id);
        });
    }
    /**
     * 取消任务调度
     * @param id
     */
    public static void cancel(String id) {
        Optional.ofNullable(scheduledFutureMap.get(id)).ifPresent(x -> {
            if (!x.isDone()) {
                x.cancel(true);
            }
        });
    }
    /**
     * 关闭任务线程次并清空所有任务
     */
    public static void shutdownNow() {
        schedule.shutdownNow();
        scheduledFutureMap.clear();
    }
}
