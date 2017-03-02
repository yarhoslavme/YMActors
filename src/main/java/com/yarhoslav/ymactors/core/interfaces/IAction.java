package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IAction {

    public void doIt(IActorMsg pMsg, IActorRef pSender) throws Exception;
}
