package com.ahua.easytaskexecutor.core;


import com.ahua.easytaskexecutor.core.handler.Handler;

/**
 * 
 * @author jason
 *
 */

public interface CompletionService extends Runnable {

	void register(Handler handler);
	void shutDown();
}
