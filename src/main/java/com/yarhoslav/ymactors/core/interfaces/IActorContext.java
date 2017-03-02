package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public ISystem getSystem();

    public void setState(IActorState pState);
    
    public IActorState getState();

}
