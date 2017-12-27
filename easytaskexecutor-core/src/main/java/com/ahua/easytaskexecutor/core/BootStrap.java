package com.ahua.easytaskexecutor.core;

import java.util.concurrent.*;

/**
 * 
 * @author jason
 *
 */
public class BootStrap {
	
	private static Worker worker0 = null;
	private Executor workExecutor = new ThreadPoolExecutor(1,1,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(10),new NamedThreadFactory("EasyWorker"),new ThreadPoolExecutor.AbortPolicy());
		
	public BootStrap() {
		
	}
	public void initWorkers(int corePoolSize,
							int maximumPoolSize,int maxQueueCapacity){
		ThreadFactory threadFactory4name = new NamedThreadFactory("EasyWorker-TaskThread");
		LinkedBlockingQueue linkedBlockingQueue4worker = new LinkedBlockingQueue<Runnable>(maxQueueCapacity);
		if(corePoolSize==0){
			corePoolSize = Runtime.getRuntime().availableProcessors()+1;
		}if(maximumPoolSize==0){
			maximumPoolSize = Runtime.getRuntime().availableProcessors()+1;
		}
		ExecutorService executor =  new ThreadPoolExecutor(corePoolSize,maximumPoolSize,0L, TimeUnit.MILLISECONDS,linkedBlockingQueue4worker,threadFactory4name,new ThreadPoolExecutor.AbortPolicy());
		worker0 = new Worker(executor);
		workExecutor.execute(worker0);
	}
	
	public static Worker getWorker0(){
		return worker0;
	}
}
