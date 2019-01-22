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
     *
     * @param corePoolSize
     *            核心线程数
     * @param maximumPoolSize
     *            最大线程数量
     * @param maxQueueCapacity
     *            队列最大容量
     * @param threadPrefix
     *            线程名称前缀
     * @param isMaster
     *            是否是主包工头
     * @param boss
     *            老板
     * @return 包工头
     */
    public Worker addWorker(int corePoolSize,
                            int maximumPoolSize,
                            int maxQueueCapacity,
                            String threadPrefix,
                            Boolean isMaster,
                            Boss boss) {
        if (threadPrefix == null) {
            threadPrefix = "EasyWorker-TaskThread";
        }
        ThreadFactory threadFactory4name = new NamedThreadFactory(threadPrefix);
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
        if (isMaster) {
            boss.addMasterWorker(newWorker);
        } else {
            boss.addSlaveWorker(newWorker);
        }
        return newWorker;
    }

    /**
     * 创建boss,可以通过重写此方法定制Boss
     *
     * @return 老板
     */
    public Boss createBoss() {
        Boss boss = Boss.getInstance();
        synchronized (boss) {
            if (boss.getBossWorkerExecutor() == null) {
                ExecutorService executor = new ThreadPoolExecutor(1,
                        1,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(10),
                        new NamedThreadFactory("EasyWorker"),
                        new ThreadPoolExecutor.AbortPolicy());
                boss.setBossWorkerExecutor(executor);
            }
        }
        return boss;
    }

    /**
     * 创建boss
     *
     * @param corePoolSize
     *            核心线程数量
     * @param maximumPoolSize
     *            最大线程数量
     * @param maxQueueCapacity
     *            队列最大容量
     * @param threadPrefix
     *            线程名称前缀
     * @return 老板
     */
    public Boss createBoss(int corePoolSize, int maximumPoolSize, int maxQueueCapacity, String threadPrefix) {
        if (threadPrefix == null) {
            threadPrefix = "EasyWorker";
        }
        Boss boss = Boss.getInstance();
        synchronized (boss) {
            if (boss.getBossWorkerExecutor() == null) {
                ExecutorService executor = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(maxQueueCapacity),
                        new NamedThreadFactory(threadPrefix),
                        new ThreadPoolExecutor.AbortPolicy());
                boss.setBossWorkerExecutor(executor);
            }
        }
        return boss;
    }

    /**
     * 获取正在干活的主包工头
     *
     * @return 正在干活的主包工头
     */
    public List<Worker> getMasterWorkers(Boss boss) {
        Queue<Worker> runningWorkerQueue = boss.getRunningMasterWorkerQueue();
        List<Worker> workers = Lists.newArrayList();
        Iterator<Worker> iterator = runningWorkerQueue.iterator();
        while (iterator.hasNext()) {
            workers.add(iterator.next());
        }
        return workers;
    }

    /**
     * 获取正在干活的从包工头
     *
     * @return 正在干活的从包工头
     */
    public List<Worker> getSlaveWorkers(Boss boss) {
        Queue<Worker> runningWorkerQueue = boss.getRunningSlaveWorkerQueue();
        List<Worker> workers = Lists.newArrayList();
        Iterator<Worker> iterator = runningWorkerQueue.iterator();
        while (iterator.hasNext()) {
            workers.add(iterator.next());
        }
        return workers;
    }

}
