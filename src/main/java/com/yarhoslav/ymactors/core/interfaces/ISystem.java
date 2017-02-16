package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.BaseActor;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public void queueUp(IWorker pWorker);

    public int getDispatcher();
    
    public String getName();
       
    public IActorRef findActor(String pName) throws IllegalArgumentException;
    
    public <E extends BaseActor> IActorRef addActor(E pActorType, String pName) throws IllegalArgumentException;
    
    public void removeActor(IActorRef pActor) throws IllegalArgumentException;

}
