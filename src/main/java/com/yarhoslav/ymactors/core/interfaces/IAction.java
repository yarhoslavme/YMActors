package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IAction {

    public void doIt(IActorMsg pMsg, IActorRef pSender) throws Exception;
}
