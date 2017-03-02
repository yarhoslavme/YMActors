package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.actions.ActionStopActor;
import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.messages.PoisonPill;

/**
 *
 * @author yarhoslavme
 */
public final class RunningState extends BaseState {

    private final BaseActor owner;

    public RunningState(BaseActor pOwner) {
        this.owner = pOwner;
        
        addBehavior(PoisonPill.getInstance(), new ActionStopActor(owner));
    }

    @Override
    public void unknownMsg(Object pMsg, IActorRef pSender) throws Exception {
        owner.process(pMsg, pSender);
    }

}
