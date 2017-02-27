package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IWorker extends Runnable {

    public void process(IActorMsg pMsg, IActorRef pSender);

    public void requestQueue();

    public int getDispatcher();

    public IEnvelope getNextMsg();

    public void newMessage(IEnvelope pMsg);
}
