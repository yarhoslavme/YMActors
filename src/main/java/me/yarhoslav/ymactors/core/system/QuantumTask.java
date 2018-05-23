package me.yarhoslav.ymactors.core.system;

import java.util.concurrent.FutureTask;

/**
 *
 * @author yarhoslavme
 */
public class QuantumTask extends FutureTask<Object>{
    
    public QuantumTask(Runnable runnable, Object result) {
        super(runnable, result);
    }
    
}
