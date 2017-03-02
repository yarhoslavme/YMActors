package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IActorState {

    public void execute(Object pMsg, IActorRef pSender) throws Exception;

    public void addBehavior(IActorMsg pMsg, IAction pAction);
}
