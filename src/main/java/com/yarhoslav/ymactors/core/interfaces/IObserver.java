package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IObserver {
    public void update(Object pEvent, Object pMsg, IActorRef pActor);    
}
