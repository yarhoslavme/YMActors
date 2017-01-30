package com.yarhoslav.ymactors.core.interfaces;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public IActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException, IllegalStateException;

    public IActorRef findActor(String pName);
    
    public IActorContext getContainer();
    
    public IActorRef getParent();
    
    public Map<String, IActorRef> getChildren();
    
    public boolean isAlive();
    
    public ExecutorService getExecutor();
    
    public void setMyself(IActorRef pMyself);
    
    public IActorRef getMyself();

}
