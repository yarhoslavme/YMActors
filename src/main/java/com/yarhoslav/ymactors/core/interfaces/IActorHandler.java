package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorHandler {

    public void preStart() throws IllegalStateException;

    public void beforeStop() throws IllegalStateException;

    public void process(Object msj) throws IllegalStateException;

    public IActorRef getMyself();

    public void setMyself(IActorRef pMyself);
}
