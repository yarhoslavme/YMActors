package com.yarhoslav.ymactors.core.actions;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.interfaces.IAction;
import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public class ActionStopActor implements IAction {

    private final BaseActor owner;

    public ActionStopActor(BaseActor pOwner) {
        owner = pOwner;
    }

    @Override
    public void doIt(Object pMsg, IActorRef pSender) throws Exception {
        owner.stop();
    }

}
