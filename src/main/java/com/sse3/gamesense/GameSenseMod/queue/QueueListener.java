/*
 * Decompiled with CFR 0.152.
 */
package com.sse3.gamesense.GameSenseMod.queue;

import com.sse3.gamesense.GameSenseMod.Constants;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueueListener {
    private static QueueListener instance;
    private static Thread workerThread;
    private static BlockingQueue<Runnable> executionQueue;
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public QueueListener() {
        executionQueue = new ArrayBlockingQueue<Runnable>(1024);
        Runnable taskExecutor = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable execution = executionQueue.take();
                    this.executor.schedule(execution, 0L, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                catch (Exception e) {
                    Constants.LOG.error("Error executing task", (Throwable)e);
                }
            }
            Constants.LOG.info("Exited Thread");
        };
        workerThread = new Thread(taskExecutor);
        workerThread.setName("GameSenseMod");
    }

    public void startListening() {
        workerThread.start();
    }

    public void enqueue(Runnable item) {
        executionQueue.add(item);
    }

    public void stopListening() {
        Constants.LOG.info("Stopping the worker thread");
        workerThread.interrupt();
        Constants.LOG.info("Stopping the executor");
        this.executor.shutdown();
    }

    public static QueueListener getInstance() {
        if (instance == null) {
            instance = new QueueListener();
        }
        return instance;
    }
}

