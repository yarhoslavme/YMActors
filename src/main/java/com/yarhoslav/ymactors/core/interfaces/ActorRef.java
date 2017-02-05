package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author YarhoslavME
 */
public interface ActorRef extends Runnable {

    public void tell(Object pData, ActorRef pSender);

    public String getName();

    public IActorContext getContext();
}
