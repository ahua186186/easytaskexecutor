package com.ahua;

import com.ahua.easytaskexecutor.core.BootStrap;
import com.ahua.easytaskexecutor.core.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test {

    public static void main(String[] args) {
        // 初始化老板和包工头 ,可以通过boss的addWork和removeWork动态的扩容和缩容
        int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        int corePoolmaximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        int maxQueueCapacity = 1024;
        // 扩容 当前机器IP执行扩容
        List<Worker> masterWorkers4Running1 = BootStrap.getMasterWorkers();
        // 是否已经上线
        if (masterWorkers4Running1 == null || masterWorkers4Running1.size() == 0) {
            BootStrap.addWorker(corePoolSize, corePoolmaximumPoolSize, maxQueueCapacity, true);
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
            List<Worker> workers = BootStrap.getMasterWorkers();
            if (workers != null && !workers.isEmpty()) {
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
        List<Worker> masterWorkers4Running = BootStrap.getMasterWorkers();
        if (masterWorkers4Running != null || masterWorkers4Running.size() > 0) {
            for (Worker work : masterWorkers4Running) {
                BootStrap.getBoss().removeMasterWorker(work);
            }
            List<Worker> slaveWorkers4Running = BootStrap.getSlaveWorkers();
            for (Worker work : slaveWorkers4Running) {
                BootStrap.getBoss().removeSlaveWorker(work);
            }
            BootStrap.getBoss().getBossWorkerExecutor().shutdown();
        }

    }

    private static List<Map<String, Object>> Mock4getTaskList(int i) {
        List<Map<String, Object>> tasklist = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new HashMap<String, Object>();
        task.put("hello", i);
        tasklist.add(task);
        return tasklist;
    }
}
