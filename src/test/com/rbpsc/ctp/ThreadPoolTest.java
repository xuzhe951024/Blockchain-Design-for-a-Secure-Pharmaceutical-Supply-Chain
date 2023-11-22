package com.rbpsc.ctp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<String> dataList = Arrays.asList("data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10");
        int maxThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
        List<Future<Long>> futures = new ArrayList<>();
        List<Long> taskTimes = new ArrayList<>();  // 用于存储每个任务的耗时

        for (String data : dataList) {
            Future<Long> future = executorService.submit(() -> {
                long startTime = System.currentTimeMillis();  // 开始时间
                // 处理数据
                System.out.println("Processing: " + data);
                long endTime = System.currentTimeMillis();  // 结束时间
                return endTime - startTime;  // 返回此任务的耗时
            });

            futures.add(future);

            if (futures.size() == maxThreads) {
                for (Future<Long> f : futures) {
                    taskTimes.add(f.get());  // 获取并存储任务耗时
                }
                futures.clear();
            }
        }

        for (Future<Long> future : futures) {
            taskTimes.add(future.get());  // 获取并存储剩余任务的耗时
        }

        executorService.shutdown();

        // 计算平均耗时
        long total = taskTimes.stream().mapToLong(Long::longValue).sum();
        double average = total / (double) taskTimes.size();
        System.out.println("Average Task Time: " + average + " ms");
    }
}
