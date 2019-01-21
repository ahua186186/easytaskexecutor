package com.ahua.easytaskexecutor.core;

import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title WorkerQueryService.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 包工头的查询接口
 * @date 2019/1/21 17:38
 */ 
public interface WorkerQueryService {
    /**
     * 线程是否停止
     * 
     * @return 线程是否停止
     */
    boolean isShutdown();

    /**
     * 获取子线程执行器
     *
     * @return 子线程执行器
     */
    ExecutorService getExecutor();

    /**
     * 获取包工头的当前工作队列
     *
     * @return 当前工作队列
     */
    Queue<Runnable> getTaskQueue();
}
