package me.yarhoslav.ymactors.core.system;

/**
 *
 * @author Yarhoslav ME
 */
public interface IQuantumExecutor {

    public int getDispatcher();

    public void submitTask(int pDispatcher, Runnable pTask);

    public void shutdown(); //TODO: Implements types of shutdown

}
