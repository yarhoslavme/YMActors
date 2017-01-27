package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ICoreMessage;

/**
 *
 * @author YarhoslavME
 */
public final class DefaultMessage implements ICoreMessage {
    private final IActorRef sender;
    private final Object data;

    @Override
    public IActorRef getSender() {
        return sender;
    }

    @Override
    public Object getData() {
        return data;
    }
    
    public DefaultMessage(final IActorRef pSender, final Object pData) {
        sender = pSender;
        data = pData;
    }   
}
