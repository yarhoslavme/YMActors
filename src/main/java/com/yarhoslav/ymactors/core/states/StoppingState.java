package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.interfaces.IAction;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.actors.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorState;

/**
 *
 * @author yarhoslavme
 */
public final class StoppingState implements  IActorState {

    @Override
    public void execute(Object pMsg, IActorRef pSender) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addBehavior(IActorMsg pMsg, IAction pAction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
