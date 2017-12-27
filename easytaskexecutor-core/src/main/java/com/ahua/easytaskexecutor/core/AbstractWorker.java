package com.ahua.easytaskexecutor.core;


import com.ahua.easytaskexecutor.core.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;


/**
 * 
 * @author jason
 *
 */
abstract class AbstractWorker implements CompletionService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//private static final AtomicInteger nextId = new AtomicInteger();
	/**
	 * 子线程执行器
	 */
	private  final Executor executor;
	
	private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();
	
    protected volatile Thread thread;

    final CountDownLatch startupLatch = new CountDownLatch(1);
    
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);
    
    private volatile boolean shutdown;
    
   public AbstractWorker(Executor executor) {
	   this.executor = executor;
   }
    
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
    
    public void run() {
    	thread = Thread.currentThread();
    	//Thread.currentThread().setName("BatchUpdateWorker"+nextId.incrementAndGet());
        startupLatch.countDown();
        
        for (;;) {
        	processTaskQueue(true);
        	try{
	        	 if (shutdown) {
	        		 // process one time again
	        		 processTaskQueue(true);
	        		 shutdownLatch.countDown();
	                 break;
	        	 }else{
	        		 //process();  //做一些其他的任务，扩展用
	        	 }
        	}catch(Throwable t){
        		logger.warn(
                        "Unexpected exception in the loop.", t);

                // Prevent possible consecutive immediate failures that lead to
                // excessive CPU consumption.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Ignore.
                }
        	}finally{//控制CPU执行频率
        		 try {
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                     // Ignore.
                 }
        	}
        }
    	
    }
    private void processTaskQueue( boolean alwaysAsync) {
        for (;;) {
        	if(taskQueue.isEmpty()){
        		 break;
        	}
            final Runnable task = taskQueue.poll();
            if(alwaysAsync){
            	executor.execute(task);
            }else{
            	task.run();
            }
        }
    }
    
    public void shutDown() {

        shutdown = true;
        try {
            shutdownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract void process(Object object) throws IOException;
    
    protected abstract Runnable createRegisterTask(Handler handler);

	public Queue<Runnable> getTaskQueue() {
		return taskQueue;
	}
}
