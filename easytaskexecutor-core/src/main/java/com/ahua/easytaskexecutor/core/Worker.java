package com.ahua.easytaskexecutor.core;


import com.ahua.easytaskexecutor.core.handler.Handler;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * 
 * @author jason
 *
 */
public class Worker extends AbstractWorker {

	public Worker(Executor executor) {
		super(executor);
	}

	@Override
	protected void process(Object object) throws IOException {
		//dosomething
	}

	@Override
	protected Runnable createRegisterTask(Handler handler) {
		return new RegisterTask(handler);
	}

}
