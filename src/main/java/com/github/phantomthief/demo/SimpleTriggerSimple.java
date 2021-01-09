package com.github.phantomthief.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.phantomthief.collection.BufferTrigger;

public class SimpleTriggerSimple {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    public static final BufferTrigger<Long> bufferTrigger = BufferTrigger
            .<Long, Map<String, Long>> simple()
            .maxBufferCount(3)
            .rejectHandler(SimpleTriggerSimple::rejectHandler)
            .consumer(SimpleTriggerSimple::doConsume)
            .setContainer(HashMap::new, SimpleTriggerSimple::addLong)
            .interval(1, TimeUnit.SECONDS)
            .build();

    private static void rejectHandler(Long aLong) {
        System.out.println("reject:" + aLong);
    }

    private static void doConsume(Map<String, Long> map) {
        System.out.println("do consume");
        System.out.println(map);
    }

    private static boolean addLong(Map<String, Long> map, Long s) {
        System.out.println("addLong:" + map);
        map.put(s.toString(), s + map.getOrDefault(s.toString(), 0L));
        return true;
    }

    public static void main(String[] args) throws InterruptedException {

        for (long i = 0; i < 10; i++) {
            bufferTrigger.enqueue(i);
            Thread.sleep(50);
        }

    }
}
