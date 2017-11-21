package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public class SimpleEnvelope implements IEnvelope {

    private final Object message;
    private final IActorRef sender;
    private final Object header;
    
    public SimpleEnvelope(Object pMsg, IActorRef pSender, Object pHeader) {
        message = pMsg;
        sender = pSender;
        header = pHeader;
    }

    @Override
    public IActorRef sender() {
        return sender;
    }

    @Override
    public Object message() {
        return message;
    }

    @Override
    public Object header() {
        return header;
    }

}
