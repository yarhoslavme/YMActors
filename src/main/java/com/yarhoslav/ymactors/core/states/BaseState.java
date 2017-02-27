package com.yarhoslav.ymactors.core.states;

import com.yarhoslav.ymactors.core.interfaces.IAction;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
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
    public void execute(IActorMsg pMsg) throws Exception {
        IAction action = behaviors.get(pMsg.id());
        if (action != null) {
            action.doIt(pMsg);
        } else {
            unknownMsg(pMsg);
        }
    }
    
    public abstract void unknownMsg(IActorMsg pMsg) throws Exception;
}
