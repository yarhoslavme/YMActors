package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class BasicMsg implements IActorMsg {
    private final Object data;
    private final ActorRef sender;
    
    public BasicMsg(Object pData, ActorRef pSender) {
        data = pData;
        sender = pSender;
    }

    @Override
    public ActorRef sender() {
        return this.sender;
    }

    @Override
    public Object takeData() {
        return this.data;
    }
    
}