package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorHandler {

    public void postStart() throws Exception;

    public void beforeStop() throws Exception;

    public void process(Object msj) throws Exception;

    public IActorRef getMyself();
    
    public void setMyself(IActorRef pMyself);
}
