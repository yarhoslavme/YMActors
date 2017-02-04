package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author YarhoslavME
 */
public interface ActorRef {

    public void tell(Object pData, ActorRef pSender);

    public String getName();

    public IActorContext getContext();
}
