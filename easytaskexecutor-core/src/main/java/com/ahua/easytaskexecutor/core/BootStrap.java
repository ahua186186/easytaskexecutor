package com.ahua.easytaskexecutor.core;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title BootStrap.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 引导程序
 * @date 2019/1/20 21:50
 */ 
public class BootStrap {
    /**
     * 添加包工头
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程数量
     * @param maxQueueCapacity 队列最大容量
     * @param isMaster 是否是主包工头
     * @return 包工头
     */
    public static Worker addWorker(int corePoolSize, int maximumPoolSize, int maxQueueCapacity,Boolean isMaster) {
        ThreadFactory threadFactory4name = new NamedThreadFactory("EasyWorker-TaskThread");
        LinkedBlockingQueue linkedBlockingQueue4worker = new LinkedBlockingQueue<Runnable>(maxQueueCapacity);
        if (corePoolSize == 0) {
            corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        }
        if (maximumPoolSize == 0) {
            maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        }
        ExecutorService executor = new ThreadPoolExecutor(corePoolSize,
                                                          maximumPoolSize,
                                                          0L,
                                                          TimeUnit.MILLISECONDS,
                                                          linkedBlockingQueue4worker,
                                                          threadFactory4name,
                                                          new ThreadPoolExecutor.AbortPolicy());
        Worker newWorker = new Worker(executor);
        if(isMaster) {
            getBoss().addMasterWorker(newWorker);
        }else{
            getBoss().addSlaveWorker(newWorker);
        }
        return newWorker;
    }

    /**
     * 获取老板
     * @return 老板
     */
    public static Boss getBoss() {
        return Boss.getInstance();
    }

    /**
     * 获取正在干活的主包工头
     * @return 正在干活的主包工头
     */
    public static List<Worker> getMasterWorkers() {
        Queue<Worker> runningWorkerQueue = getBoss().getRunningMasterWorkerQueue();
        List<Worker> workers = Lists.newArrayList();
        Iterator<Worker> iterator = runningWorkerQueue.iterator();
        while (iterator.hasNext()) {
            workers.add(iterator.next());
        }
        return workers;
    }

    /**
     * 获取正在干活的从包工头
     * @return 正在干活的从包工头
     */
    public static List<Worker> getSlaveWorkers() {
        Queue<Worker> runningWorkerQueue = getBoss().getRunningSlaveWorkerQueue();
        List<Worker> workers = Lists.newArrayList();
        Iterator<Worker> iterator = runningWorkerQueue.iterator();
        while (iterator.hasNext()) {
            workers.add(iterator.next());
        }
        return workers;
    }

}
