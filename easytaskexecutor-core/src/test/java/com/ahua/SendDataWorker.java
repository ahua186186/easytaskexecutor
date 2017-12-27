/*
package com.ahua;

import com.easy.core.BootStrap;
import com.easy.core.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.CountDownLatch;

*/
/**
 * 
 * @author jason
 *
 *//*

public class SendDataWorker extends Thread implements InitializingBean{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    protected volatile Thread thread;

    final CountDownLatch startupLatch = new CountDownLatch(1);
    
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);
    
    private volatile boolean shutdown;
    //format: "pid@hostname"
    public static String localIP = ManagementFactory.getRuntimeMXBean().getName();
    
    private Worker worker = null;
    
    private BootStrap bs=null;
    
    private boolean initStatus;
    
    public SendDataWorker() {
    	
    }

    public void run() {
    	thread = Thread.currentThread();
    	Thread.currentThread().setName("SendDataWorker"+ new Random().nextInt(100));
        startupLatch.countDown();
        
        init();

        for(;;){
	        process();
	    	try{
	        	 if (shutdown) {
	        		 shutdownLatch.countDown();
	        		 break;
	        	 }
	    	}catch(Throwable t){
	    		logger.warn("Unexpected exception in the loop.", t);
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                // Ignore.
	            }
	    	}finally{//控制CPU执行频率
	    		 try {
	                 Thread.sleep(1000*10);
	             } catch (InterruptedException e) {
	                 // Ignore.
	             }
	    	}
        }
    	
    }
    private void process() {
        //随机获取数据的机器号，默认有4个应用进程竞争待发送的数据的机器号。
    	*/
/*try{
	    	String code = ClientProxy.get(ILgtNbExpAction.class).updateAndGetMachineCode4CMSPIN(localIP);
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	map.put("sendip", code);
	    	map.put("result", "I");
	    	map.put("pageCount", 1);//一次取一页数据
	    	List<Map<String, Object>> taskList=	ClientProxy.get(ILgtNbExpAction.class).queryNbgGoodsRecord(map);
	    	logger.info("===================taskList size: "+taskList.size());
	    	if(taskList!=null && taskList.size()>0){
		    	List<Long> ids = new ArrayList<Long>();
		    	for(Map<String, Object> item : taskList){
		    		ids.add(Long.valueOf(item.get("id").toString()));
		    	}
		    	ClientProxy.get(ILgtNbExpAction.class).updateResult4NbgGoodsRecordByid(ids, "ing");
		    	CountDownLatch cdl = new CountDownLatch(taskList.size());
		    	logger.info("===================CountDownLatch start: ");
		    	for(Map<String, Object> taskMap : taskList){//dispach task,默认一条数据一个task
		    		List<Map<String, Object>> task = new ArrayList<Map<String, Object>>();
		    		task.add(taskMap);
		    		
			    	SendDataHandler handler = new SendDataHandler(cdl);
			    	handler.setTaskList(task);
			    	worker.register(handler);
		    	}
		    	if(cdl.getCount()>0){
		    		cdl.await();
		    	}
		    	logger.info("===================CountDownLatch end: ");
	    	}
    	}catch(Exception ex){
    		logger.error("process error:", ex);
    	}*//*

    	
    	
    	
    	
    	DefaultHandler handler = new DefaultHandler();
    	List<Map<String, Object>> tasklist = new ArrayList<Map<String,Object>>();
    	for(int i=0;i<1000;i++){
	    	Map<String, Object> task = new HashMap<String, Object>();
	    	task.put("hello", i);
	    	tasklist.add(task);
    	}
    	handler.setTaskList(tasklist);
    	worker.register(handler);

    }
    
    public void shutDown() {

        shutdown = true;
        try {
            shutdownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


	@Override
	public void afterPropertiesSet() throws Exception {
		//应用启动时清空配置表的机器号
		*/
/*try{
			ClientProxy.get(ILgtNbExpAction.class).clearMachineCode4CMSPIN();
			initStatus= true;
		}catch(Exception ex){
			initStatus= false;
			logger.error("初始化失败", ex);
		}*//*

    	this.start();
	}
	
	public void close() {
		shutDown();
	}
	
	private void init(){
		while(true){
        	if(initStatus){
        		break;
        	}
			try {
				Thread.sleep(100);
			    //Thread.sleep(1000*60);
			}catch (InterruptedException e) {
			    // Ignore.
			}
			try{
				//ClientProxy.get(ILgtNbExpAction.class).clearMachineCode4CMSPIN();
				initStatus=true;
			}catch(Exception ex){
				initStatus= false;
				logger.error("初始化失败", ex);
			} 
        }
		
		bs = new BootStrap();
	    bs.initWorkers(10);
	    worker = BootStrap.getWorker0();
	}
   
}
*/
