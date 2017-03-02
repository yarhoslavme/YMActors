package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.interfaces.IAction;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorState;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseState implements IActorState {

    private final Map<String, IAction> behaviors = new HashMap();

    @Override
    public void addBehavior(IActorMsg pMsg, IAction pAction) {
        behaviors.putIfAbsent(pMsg.id(), pAction);
    }

    @Override
    public void execute(Object pMsg, IActorRef pSender) throws Exception {
        if (pMsg instanceof IActorMsg) {
            IActorMsg msg = (IActorMsg) pMsg;
            IAction action = behaviors.get(msg.id());
            if (action != null) {
                action.doIt(msg, pSender);
            }
        } else {
            unknownMsg(pMsg, pSender);
        }
    }
    
    public abstract void unknownMsg(Object pMsg, IActorRef pSender) throws Exception;
}
