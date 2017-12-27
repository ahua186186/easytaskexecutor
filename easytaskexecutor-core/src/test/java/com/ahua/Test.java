package com.ahua;

import com.ahua.easytaskexecutor.core.BootStrap;
import com.ahua.easytaskexecutor.core.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {

		BootStrap bs = new BootStrap();
		int threadCount = Runtime.getRuntime().availableProcessors()+1;
		bs.initWorkers(threadCount,threadCount,1024);
		Worker worker = BootStrap.getWorker0();

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
}
