package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class BasicMsg implements IActorMsg {
    private final Object data;
    private final IActorRef sender;
    
    public BasicMsg(Object pData, IActorRef pSender) {
        data = pData;
        sender = pSender;
    }

    @Override
    public IActorRef sender() {
        return this.sender;
    }

    @Override
    public Object takeData() {
        return this.data;
    }
    
}