package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.ActorsContainer;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef extends Runnable {

    public void tell(Object pData, IActorRef pFuente);

    public String getName();

    public void start();

    public ActorsContainer getContainer();

    public IActorRef getSender();
    
    public ConcurrentHashMap<String, Object> getContext();
}
