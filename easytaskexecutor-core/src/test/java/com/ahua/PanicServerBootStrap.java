package com.ahua;

import com.ahua.easytaskexecutor.core.BootStrap;
import com.ahua.easytaskexecutor.core.Boss;
import com.ahua.easytaskexecutor.core.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title PanicServerBootStrap.java
 * @Package com.ahua
 * @Description 测试
 * @date 2019/1/22 18:29
 */
public class PanicServerBootStrap extends BootStrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(PanicServerBootStrap.class);

    /**
     * 最小线程数量，指定线程池的工作线程数量时必须大于这个数量，否则设置无效。
     */
    private static final Integer MINIMUM_THREAD_COUNT = 1;

    /**
     * 默认的队列容量，暂时不开放设置
     */
    private static final Integer DEFAULT_MAX_QUEUE_CAPACITY = 1024;
    /**
     * 老板
     */
    private Boss boss;

    /**
     * 拉单server的单例
     */
    private static class InstanceHolder {
        public static PanicServerBootStrap panicServerBootStrap = new PanicServerBootStrap();
    }

    /**
     * 获取线程安全的单例
     *
     * @return 拉单服务器实例
     */
    public static PanicServerBootStrap getInstance() {
        return PanicServerBootStrap.InstanceHolder.panicServerBootStrap;
    }

    /**
     * 初始化老板
     */
    public PanicServerBootStrap initBoss() {
        this.boss = this.createBoss();
        return this;
    }

    /**
     * 获取老板
     * 
     * @return
     */
    public Boss getBoss() {
        return boss;
    }

    /**
     * 计算密集型任务不建议指定线程数量
     * 
     * @param maxThreadCount
     *            指定线程数量
     * @return 上线是否成功
     */
    public Boolean goOnline(int maxThreadCount) {
        try {
            int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
            int corePoolmaximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
            if (maxThreadCount > MINIMUM_THREAD_COUNT) {
                corePoolSize = maxThreadCount;
                corePoolmaximumPoolSize = maxThreadCount;
            }
            int maxQueueCapacity = DEFAULT_MAX_QUEUE_CAPACITY;
            // 扩容 当前机器IP执行扩容
            List<Worker> masterWorkers4Running1 = getMasterWorkers(boss);
            // 是否已经上线
            if (masterWorkers4Running1 != null && masterWorkers4Running1.size() == 0) {
                addWorker(corePoolSize,
                          corePoolmaximumPoolSize,
                          maxQueueCapacity,
                          "panicbuy-EasyWorker-TaskThread",
                          true,
                          boss);
            }
        }
        catch (Exception ex) {
            LOGGER.error("PanicServerBootStrap.goOnline failed:", ex);
            return false;
        }
        return true;
    }

    /**
     * 下线
     * 
     * @return 下线是否成功
     */
    public Boolean offline() {
        try {
            List<Worker> masterWorkers4Running = getMasterWorkers(boss);
            List<Worker> slaveWorkers4Running = getSlaveWorkers(boss);
            if (masterWorkers4Running != null) {
                for (Worker work : masterWorkers4Running) {
                    boss.removeMasterWorker(work);
                }
            }
            if (slaveWorkers4Running != null) {
                for (Worker work : slaveWorkers4Running) {
                    boss.removeSlaveWorker(work);
                }
            }
            boss.getBossWorkerExecutor().shutdown();
        }
        catch (Exception ex) {
            LOGGER.error("PanicServerBootStrap.offline failed:", ex);
            return false;
        }
        return true;
    }

}
