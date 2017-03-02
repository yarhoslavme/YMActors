package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IWorker extends Runnable {

    public void requestQueue();

    public int getDispatcher();

    public IEnvelope getNextMsg();

    public void newMessage(IEnvelope pMsg);
}
