package com.yarhoslav.ymactors.core.services;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author yarhoslavme
 */
public class YMExecutorService {
    //TODO: Check health of the threadpoolexecutors
    //TODO: Implement steal jobs or better dispatcher giver based on job queue size
    
    private final Logger logger = LoggerFactory.getLogger(YMExecutorService.class);
    private final ExecutorService[] pool;
    private final BlockingQueue<Runnable>[] queues;
    private final int threads;
    private final AtomicInteger index;
    public final AtomicInteger mensajes = new AtomicInteger(0);

    public YMExecutorService(int pThreads) {
        threads = pThreads;
        pool = new ExecutorService[threads];
        queues = new BlockingQueue[threads];
        index = new AtomicInteger(0);
        for (int i = 0; i < threads; i++) {
            queues[i] = new LinkedBlockingQueue<>();
            pool[i] = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queues[i], new DaemonThreadFactory());
        }
    }

    public void offer(IWorker pTask) {
        mensajes.incrementAndGet();
        pool[pTask.getDispatcher()].execute(pTask);
    }

    public int getDispacher() {
        //TODO: Take care of integer.MAX in index.
        int disp = index.getAndIncrement() % threads;
        logger.info("Dispacher->{}", disp);
        return disp;

    }

    @Override
    public String toString() {
        String salida = "YMExecutorService:";
        for (int i = 0; i < threads; i++) {
            salida = salida + " " + i + "->" + queues[i].size();
        }
        return salida;
    }
}
