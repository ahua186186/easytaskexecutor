package com.ahua;

import com.ahua.easytaskexecutor.core.WorkerQueryService;
import com.ahua.easytaskexecutor.core.handler.Handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DefaultHandler implements Handler {
    private CountDownLatch cdl4test;
    /**
     * 任务列表
     */
    private List<Map<String, Object>> taskList;
    /**
     * 包工头，这里主要是用于查询工作线程的状态,然后优雅的缩容,因为线程池shutdown的时候会一直等待任务处理完
     */
    private WorkerQueryService worker;

    public boolean doBizBefore() {
        return true;
    }

    public boolean processInThread() {
        long startTime = System.currentTimeMillis();
        try {
            // 典型的CPU密集型任务
            for (;;) {
                System.out.println("========do something========");
                for (Map<String, Object> map : taskList) {
                    System.out.println(map.get("hello"));
                    if (Integer.parseInt(map.get("hello").toString()) == 1000000) {
                        System.out.println("perfect");
                    }
                }
                if (worker != null && worker.isShutdown()) {
                    break;
                }
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    // Ignore.
                }
                // TEST
                break;
            }
        }
        catch (Exception localException) {
            localException.printStackTrace();
        }
        finally {
            long endTime = System.currentTimeMillis();
            System.out.println("=============spend time:" + ((endTime - startTime)) + "ms" + "=============");
            cdl4test.countDown();
        }
        return true;
    }

    public boolean doBizAfter() {
        taskList = null;
        // 统计+监控
        return true;
    }

    @Override
    public void setWorker(WorkerQueryService worker) {
        this.worker = worker;
    }

    public List<Map<String, Object>> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Map<String, Object>> taskList) {
        this.taskList = taskList;
    }

    public void setCdl(CountDownLatch cdl) {
        this.cdl4test = cdl;
    }
}
