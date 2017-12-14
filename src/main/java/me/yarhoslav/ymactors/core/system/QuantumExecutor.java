package me.yarhoslav.ymactors.core.system;

import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author yarhoslavme
 */
public class QuantumExecutor extends ForkJoinPool {
    //TODO: Manage the quantum execution and assign new ticket
    
    public QuantumExecutor() {
        //TODO: Provides a Handler for uncautch execptions.  (Replace NULL parameter)
        super(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    }
}