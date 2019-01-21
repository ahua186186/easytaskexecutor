package com.ahua.easytaskexecutor.core;


import com.ahua.easytaskexecutor.core.handler.Handler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title Worker.java
 * @Package com.ahua.easytaskexecutor.core
 * @Description 包工头
 * @date 2019/1/20 21:54
 */
public class Worker extends AbstractWorker {

	public Worker(ExecutorService executor) {
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
