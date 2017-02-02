package com.yarhoslav.ymactors.core.interfaces;

import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public IActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException, IllegalStateException;

    public IActorRef findActor(String pName);

    public void forgetActor(IActorRef pActor);
    
    public void queueUp(IActorRef pActor);
    
    public Iterator getChildren();

    public IActorContext getContainer();

    public IActorRef getParent();
    
    public IActorRef getMyself();
    
    public void setMyself(IActorRef pActor);

}
