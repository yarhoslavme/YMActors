package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author YarhoslavME
 */
public interface IActorMsg {

    public IActorRef sender();

    public Object takeData();
    
    public String id();
}
