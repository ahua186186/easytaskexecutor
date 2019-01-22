package com.ahua;

import com.ahua.easytaskexecutor.core.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test {
/**
 * @author Jason.Shen
 * @version: V1.0
 * @Title Test.java
 * @Package com.ahua
 * @Description 
 * @date 2019/1/22 18:36
 */ 
    public static void main(String[] args) {
        // 初始化老板和包工头 ,可以通过boss的addWork和removeWork动态的扩容和缩容
        int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        int corePoolmaximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        int maxQueueCapacity = 1024;
        // 扩容 当前机器IP执行扩容
        PanicServerBootStrap panicServer = PanicServerBootStrap.getInstance().initBoss();
        panicServer.goOnline(corePoolmaximumPoolSize);
        // 反复上线检测线程池是否重复增加
        for (int a = 1; a < 1000; a++) {
            new Runnable() {
                @Override
                public void run() {
                    PanicServerBootStrap panicServer2 = PanicServerBootStrap.getInstance().initBoss();
                    panicServer2.goOnline(9);
                }
            }.run();
        }
        /************************ 给包工头分配CPU密集型任务包start ************************/

        // 测试100万个订单,消息是否丢失
        int testNumber = 1000000;
        CountDownLatch cdl = new CountDownLatch(testNumber);
        for (int i = 1; i <= testNumber; i++) {
            DefaultHandler handler = new DefaultHandler();
            // 从数据源(redis,db,file等)取任务元数据或者任务数据
            List<Map<String, Object>> tasklist = Mock4getTaskList(i);
            handler.setTaskList(tasklist);
            handler.setCdl(cdl);
            // 随机发送任务包给包工头
            List<Worker> workers = panicServer.getMasterWorkers(panicServer.getBoss());
            if (workers != null && workers.size() > 0) {
                Collections.shuffle(workers);
                Worker worker = workers.get(0);
                handler.setWorker(worker);
                worker.register(handler);
            }
        }
        try {
            cdl.await();
        }
        catch (Exception ex) {

        }
        System.out.println("===========test success===============");
        /************************ 给包工头分配CPU密集型任务包end ************************/

        // 缩容 当前机器IP执行下线
        panicServer.offline();

    }

    private static List<Map<String, Object>> Mock4getTaskList(int i) {
        List<Map<String, Object>> tasklist = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new HashMap<String, Object>();
        task.put("hello", i);
        tasklist.add(task);
        return tasklist;
    }
}
