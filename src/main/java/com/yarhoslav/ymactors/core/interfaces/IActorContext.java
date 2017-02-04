package com.yarhoslav.ymactors.core.interfaces;

import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public ActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException, IllegalStateException;

    public ActorRef findActor(String pName);

    public void forgetActor(ActorRef pActor);
    
    public void queueUp(ActorRef pActor);
    
    public Iterator getChildren();

    public IActorContext getSystem();

    public ActorRef getParent();
    
    public ActorRef getMyself();
    
    public void setMyself(ActorRef pActor);

}
