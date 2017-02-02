package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef extends Runnable {

    public void tell(Object pData, IActorRef pSender);

    public IActorRef start() throws IllegalStateException;

    public String getName();

    public IActorContext getContext();

    public boolean isAlive();
}
