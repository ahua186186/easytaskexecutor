package com.ahua.easytaskexecutor.core;

import com.ahua.easytaskexecutor.core.handler.Handler;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title WorkerCompletionService.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 包工头的辅助接口
 * @date 2019/1/21 14:13
 */
public interface WorkerCompletionService {
	/**
	 * 注册任务到包工头的任务队列中
	 * @param handler
	 */
    void register(Handler handler);

	/**
	 * 优雅的辞退包工头
	 */
    void shutDown();
}
