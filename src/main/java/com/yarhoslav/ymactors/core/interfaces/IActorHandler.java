package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorHandler {

    public void preStart() throws Exception;

    public void beforeStop() throws Exception;

    public void process(Object msj, IActorRef pSender) throws Exception;
    
    public void handleException(Exception pException, IActorRef pChild);

    public IActorRef getMyself();

    public void setMyself(IActorRef pMyself);
}
