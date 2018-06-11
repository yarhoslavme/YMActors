package me.yarhoslav.ymactors.core.system;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author Yarhoslav ME
 */
public interface IQuantumExecutor {

    int getDispatcher();

    void submitTask(int pDispatcher, Runnable pTask) throws RejectedExecutionException, NullPointerException;

    void shutdown(); //TODO: Implements types of shutdown

}
