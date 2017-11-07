package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IObserver {
    public void update(Object pEvent, Object pMsg, IActorRef pActor);    
}
