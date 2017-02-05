package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.ActorSystem;
import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public ActorRef newActor(Class pActorType, String pName) throws IllegalArgumentException, IllegalStateException;

    public ActorRef findActor(String pName);

    public void forgetActor(ActorRef pActor);
    
    public Iterator getChildren();

    public ActorSystem getSystem();

    public ActorRef getParent();
    
    public void requestQueue();
    
    public void dequeue();

}
