package com.ahua.easytaskexecutor.core;

import com.ahua.easytaskexecutor.core.handler.Handler;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title RegisterTask.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 任务线程
 * @date 2019/1/21 14:49
 */
public class RegisterTask implements Runnable {
    private Handler handler;

    RegisterTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (handler.doBizBefore()) {
            handler.processInThread();
        } else {
            // record logs
        }
        handler.doBizAfter();
    }

}