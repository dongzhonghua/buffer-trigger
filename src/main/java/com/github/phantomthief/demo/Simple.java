package com.github.phantomthief.demo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.phantomthief.collection.BufferTrigger;

public class Simple {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    public static final BufferTrigger<String> bufferT = BufferTrigger
            .<String> batchBlocking()
            .batchSize(10)
            .linger(5, TimeUnit.SECONDS)
            .setConsumerEx((List<String> list) ->
                    list.forEach(v -> EXECUTOR_SERVICE.execute(() -> doBatch(v))))
            .build();

    private static void doBatch(String v) {
        System.out.println(v);
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            bufferT.enqueue("test" + i);
            if (i % 1 == 0) {
                System.out.println("=============");
                Thread.sleep(5000);
            }
            //                        Thread.sleep(3000);
        }
    }
}
