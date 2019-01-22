package com.ahua.easytaskexecutor.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author Jason.shen
 * @version V1.0
 * @Title: Boss.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 老板
 * @date 2019 01-20 21:04.
 */
public class Boss {
    /**
     * 正在工作的主包工头队列,用于计算密集型任务 计算密集型任务同时进行的数量应当等于CPU的核心数+1(经验值)
     */
    private final Queue<Worker> runningWorkerQueueForCPU = new ConcurrentLinkedQueue<Worker>();
    /**
     * 正在工作的从包工头队列,用于IO密集型任务 暂时没什么用,这里的设计是方便以后可能有CPU任务-->IO任务之间的互转（通常用于网络通讯或异步转发）
     */
    private final Queue<Worker> runningWorkerQueueForIO = new ConcurrentLinkedQueue<Worker>();
    /**
     * 老板的工作线程队列
     */
    private ExecutorService bossWorkerExecutor;

    private static class InstanceHolder {
        public static Boss boss = new Boss();
    }

    /**
     * 获取线程安全的单例
     * 
     * @return 老板
     */
    public static Boss getInstance() {
        return InstanceHolder.boss;
    }

    /**
     * 添加主包工头
     * 
     * @param worker
     */
    public void addMasterWorker(Worker worker) {
        if (worker == null) {
            return;
        }
        getBossWorkerExecutor().execute(worker);
        getRunningMasterWorkerQueue().add(worker);
    }

    /**
     * 添加从包工头
     *
     * @param worker
     */
    public void addSlaveWorker(Worker worker) {
        if (worker == null) {
            return;
        }
        getBossWorkerExecutor().execute(worker);
        getRunningSlaveWorkerQueue().add(worker);
    }

    /**
     * 删除主包工头
     * 
     * @param worker
     */
    public void removeMasterWorker(Worker worker) {
        if (worker == null) {
            return;
        }
        worker.shutDown();
        getRunningMasterWorkerQueue().remove(worker);
        // 当线程池调用该方法时,线程池的状态则立刻变成SHUTDOWN状态。
        // 此时，则不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。
        // 但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出
        worker.getExecutor().shutdown();
    }

    /**
     * 删除从包工头
     *
     * @param worker
     */
    public void removeSlaveWorker(Worker worker) {
        if (worker == null) {
            return;
        }
        worker.shutDown();
        getRunningSlaveWorkerQueue().remove(worker);
        // 当线程池调用该方法时,线程池的状态则立刻变成SHUTDOWN状态。
        // 此时，则不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。
        // 但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出
        worker.getExecutor().shutdown();
    }

    /**
     * 获取正在工作的主包工头的工作队列
     * 
     * @return 正在工作的主包工头的工作队列
     */
    public Queue<Worker> getRunningMasterWorkerQueue() {
        return runningWorkerQueueForCPU;
    }

    /**
     * 获取正在工作的从包工头的工作队列
     *
     * @return 正在工作的从包工头的工作队列
     */
    public Queue<Worker> getRunningSlaveWorkerQueue() {
        return runningWorkerQueueForIO;
    }

    /**
     * 获取老板的工作线程执行器
     *
     * @return 工作线程执行器
     */
    public ExecutorService getBossWorkerExecutor() {
        return bossWorkerExecutor;
    }


    /**
     * 设置老板的工作线程执行器
     *
     * @param bossWorkerExecutor
     *            工作线程执行器
     */
    public void setBossWorkerExecutor(ExecutorService bossWorkerExecutor) {
        this.bossWorkerExecutor = bossWorkerExecutor;
    }
}
