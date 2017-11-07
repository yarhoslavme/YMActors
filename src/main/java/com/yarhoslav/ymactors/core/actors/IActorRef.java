package com.yarhoslav.ymactors.core.actors;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef {

    public void tell(Object pData, IActorRef pSender) throws IllegalStateException;

    public String getName();

    public String getAddress();
}
