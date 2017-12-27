package com.ahua.easytaskexecutor.core;


import com.ahua.easytaskexecutor.core.handler.Handler;

public   class RegisterTask implements Runnable {
    private Handler handler;
    
    RegisterTask(Handler handler){
        this.handler = handler;
    }

    public void run() {
        if(handler.doBizBefore()){
        	handler.processInThread();
        }else{
        	//record logs	
        }
        handler.doBizAfter();
    }
}