package com.yarhoslav.ymactors.core.services;

import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public class YMExecutorService {

    private final Logger logger = LoggerFactory.getLogger(YMExecutorService.class);
    private final ThreadPoolExecutor[] pool;
    private final int threads;
    private final AtomicInteger index;
    public final AtomicInteger mensajes = new AtomicInteger(0);

    public YMExecutorService(int pThreads) {
        threads = pThreads;
        pool = new ThreadPoolExecutor[threads];
        index = new AtomicInteger(0);
        for (int i = 0; i < threads; i++) {
            pool[i] = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        }
    }

    public void offer(ActorRef pTask) {
        //logger.info("Cola {}, size {}", index.get(), pool[index.get()].getQueue().size());
        mensajes.incrementAndGet();
        pool[pTask.getDispatcher()].execute(pTask);
        logger.info("Ofreciendo a dispatcher->{} actor {} y el dispatcher tiene una cola de {}",pTask.getDispatcher(), pTask.getName(), pool[pTask.getDispatcher()].getQueue().size());
    }

    public int getDispacher() {
        int disp = index.getAndIncrement() % threads;
        logger.info("Dispacher->{}",disp);
        return disp;

    }
}
