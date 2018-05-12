package me.yarhoslav.ymactors.core.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author yarhoslavme
 */
public class QuantumExecutor implements IQuantumExecutor {

    private final ExecutorService executors[];
    private final AtomicInteger executorIndex;
    private final int coresAvailables;
    //TODO: Manage the quantum execution and assign new ticket
    //TODO: Implement states

    public QuantumExecutor() {
        //TODO: Cores should be an input parameter to the constructor.
        coresAvailables = Runtime.getRuntime().availableProcessors();
        executors = new ExecutorService[coresAvailables];
        //TODO: Provides a Handler for uncautch execptions.  (Replace NULL parameter)
        initializeDispatchers();
        executorIndex = new AtomicInteger(0);
    }

    private void initializeDispatchers() {
        for (int i = 0; i < executors.length; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void submitTask(int pDispatcher, Runnable pTask) {
        executors[pDispatcher].submit(pTask);
    }

    @Override
    public int getDispatcher() {
        //TODO: Configurable select next dispatcher strategy
        if (executorIndex.get() == Integer.MAX_VALUE) {
            executorIndex.set(0);
        } else {
            executorIndex.incrementAndGet();
        }
        return executorIndex.get() % coresAvailables;
    }

    @Override
    public void shutdown() {
        //TODO: Improve shutdown system implementation.
        for (ExecutorService executor : executors) {
            executor.shutdown();
        }
    }
}
