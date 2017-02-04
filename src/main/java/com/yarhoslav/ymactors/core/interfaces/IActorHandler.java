package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorHandler {

    public void preStart() throws Exception;

    public void beforeStop() throws Exception;

    public void process(Object msj, ActorRef pSender) throws Exception;
    
    public void handleException(Exception pException, ActorRef pChild);

    public ActorRef getMyself();

    public void setMyself(ActorRef pMyself);
}
