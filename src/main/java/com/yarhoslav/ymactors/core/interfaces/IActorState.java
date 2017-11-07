package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IActorState {

    public void execute(Object pMsg, IActorRef pSender) throws Exception;

    public void addBehavior(IActorMsg pMsg, IAction pAction);
}
