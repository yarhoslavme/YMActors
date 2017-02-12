package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public void queueUp(IWorker pWorker);

    public int getDispatcher();
    
    public String getName();

}
