package com.ahua;



import com.ahua.easytaskexecutor.core.handler.Handler;

import java.util.List;
import java.util.Map;


public class DefaultHandler implements Handler {
	private List<Map<String, Object>> taskList;

	public boolean doBizBefore() {
		//checkData
		return true;
	}


	public boolean processInThread() {
		long startTime = System.currentTimeMillis();
	 	try{
	 		System.out.println("========do something....");
			for(Map<String, Object> map : taskList){
				System.out.println(map.get("hello"));
			}
	    }catch (Exception localException){
			localException.printStackTrace();
		}finally{
	    	long endTime = System.currentTimeMillis();
	    	System.out.println("=============spend time:"+((endTime-startTime))+"ms");
		}
		return true;
	}


	public boolean doBizAfter() {
		taskList=null;
		//统计+监控
		return true;
	}

	public List<Map<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Map<String, Object>> taskList) {
		this.taskList = taskList;
	}
	

}
