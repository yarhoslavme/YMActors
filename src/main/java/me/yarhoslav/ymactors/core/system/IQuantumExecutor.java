package me.yarhoslav.ymactors.core.system;

import java.util.concurrent.RejectedExecutionException;

/**
 *
 * @author Yarhoslav ME
 */
public interface IQuantumExecutor {

    public int getDispatcher();

    public void submitTask(int pDispatcher, Runnable pTask) throws RejectedExecutionException, NullPointerException;

    public void shutdown(); //TODO: Implements types of shutdown

}
