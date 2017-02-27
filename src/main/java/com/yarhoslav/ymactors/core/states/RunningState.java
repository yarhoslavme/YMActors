package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;

/**
 *
 * @author yarhoslavme
 */
public final class RunningState extends BaseState {

    private final BaseActor owner;

    @Override
    public void unknownMsg(IActorMsg pMsg) throws Exception {
        owner.process(pMsg.takeData(), pMsg.sender());
    }

    public RunningState(BaseActor pOwner) {
        this.owner = pOwner;
    }

}
