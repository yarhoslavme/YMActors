package com.yarhoslav.ymactors.core.interfaces;

import java.util.HashMap;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef extends Runnable {

    public String getName();

    public IActorContext getContext();

    public IActorRef getParent();

    public HashMap<String, IActorRef> getChildren();

    public IActorRef getSender();

    public boolean isAlive();
    
    public boolean isIdle();

    public void tell(Object pData, IActorRef pSender);
    
    public IActorRef start();
}
