package com.ahua.easytaskexecutor.core;

import com.ahua.easytaskexecutor.core.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title AbstractWorker.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 包工头执行任务包
 * @date 2019/1/21 14:09
 */
abstract class AbstractWorker implements WorkerCompletionService, WorkerQueryService, Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 子线程执行器
     */
    private final ExecutorService executor;
    /**
     * 支持并发安全的任务队列，FIFO
     */
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();
    /**
     * 当前线程，目前没有太大用处
     */
    protected volatile Thread thread;
    /**
     * 线程启动倒数闸门
     */
    final CountDownLatch startupLatch = new CountDownLatch(1);
    /**
     * 线程停止倒数闸门
     */
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);
    /**
     * 线程是否停止
     */
    private volatile boolean shutdown;

    public AbstractWorker(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void register(Handler handler) {
        Runnable task = createRegisterTask(handler);
        registerTask(task);
    }

    protected final void registerTask(Runnable task) {
        taskQueue.add(task);

        if (shutdown) {
            if (taskQueue.remove(task)) {
                // the Worker has already been shutdown.
                throw new RejectedExecutionException("Worker has already been shutdown");
            }
        }
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
        startupLatch.countDown();

        for (;;) {
            processTaskQueue(true);
            try {
                if (shutdown) {
                    // process one time again
                    processTaskQueue(true);
                    shutdownLatch.countDown();
                    break;
                } else {
                    // 做一些其他的任务，扩展用
                    // process();
                }
            }
            catch (Throwable t) {
                logger.warn("Unexpected exception in the loop.", t);

                // Prevent possible consecutive immediate failures that lead to
                // excessive CPU consumption.
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    // Ignore.
                }
            }
            finally {
                // 控制CPU执行频率
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                    // Ignore.
                }
            }
        }

    }

    /**
     * 执行任务
     * 
     * @param alwaysAsync
     *            是否异步执行
     */
    private void processTaskQueue(boolean alwaysAsync) {
        for (;;) {
            if (taskQueue.isEmpty()) {
                break;
            }
            final Runnable task = taskQueue.poll();
            if (alwaysAsync) {
                try {
                    executor.execute(task);
                }
                catch (RejectedExecutionException ex) {
                    // AbortPolicy饱和策略容错,确保消息不丢失
                    taskQueue.add(task);
                }
            } else {
                task.run();
            }
        }
    }

    @Override
    public void shutDown() {
        shutdown = true;
        try {
            shutdownLatch.await();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 做一些其他的任务，扩展用
     * 
     * @param object
     *            扩展参数
     * @throws IOException
     *             抛出IO异常
     */
    protected abstract void process(Object object) throws IOException;

    /**
     * 创建注册任务
     * 
     * @param handler
     *            任务处理对象
     * @return 任务包
     */
    protected abstract Runnable createRegisterTask(Handler handler);

    /**
     * 获取包工头的当前工作队列
     * 
     * @return 当前工作队列
     */
    @Override
    public Queue<Runnable> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 获取子线程执行器
     * 
     * @return 子线程执行器
     */
    @Override
    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * 线程是否停止
     * 
     * @return 线程是否停止
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }
}
