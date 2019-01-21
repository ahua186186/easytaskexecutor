package com.ahua.easytaskexecutor.core.handler;

import com.ahua.easytaskexecutor.core.WorkerQueryService;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title Handler.java
 * @Package com.ahua.easytaskexecutor.core.handler
 * @Description 
 * @date 2019/1/21 14:11
 */ 
public interface Handler {
	/**
	 * 任务包执行前置处理
	 * @return 是否成功
	 */
	public boolean doBizBefore();

	/**
	 * 在线程中执行任务包
	 * @return
	 */
	public boolean processInThread();

	/**
	 * 任务包执行后置处理，通常用来做统计和监控
	 * @return 是否成功
	 */
	public boolean doBizAfter();

	/**
	 * 设置只有查询权限的包工头,只能查询,不能有其他操作.
	 * @param worker 只有查询权限的包工头
	 */
	public void setWorker(WorkerQueryService worker);

}
