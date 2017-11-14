package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class SimpleEnvelope implements IEnvelope {

    private final Object message;
    private final IActorRef sender;
    
    public SimpleEnvelope(Object pMsg, IActorRef pSender) {
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
