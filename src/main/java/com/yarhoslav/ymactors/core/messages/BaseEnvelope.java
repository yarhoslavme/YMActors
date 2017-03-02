package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public final class BaseEnvelope implements IEnvelope {

    private final Object message;
    private final IActorRef sender;
    
    public BaseEnvelope(Object pMsg, IActorRef pSender) {
        message = pMsg;
        sender = pSender;
    }

    @Override
    public IActorRef sender() {
        return sender;
    }

    @Override
    public Object message() {
        return message;
    }

}
