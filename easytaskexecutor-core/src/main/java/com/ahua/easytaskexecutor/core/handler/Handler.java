package com.ahua.easytaskexecutor.core.handler;

public interface Handler {
	//checkData
	public boolean doBizBefore();
	//do something
	public boolean processInThread();
	//统计+监控
	public boolean doBizAfter();
}
