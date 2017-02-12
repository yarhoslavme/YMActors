package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class ErrorMsg implements IActorMsg {
    private final BasicMsg msg;
    
    public ErrorMsg(final Exception pData, final IActorRef pSender) {
        msg = new BasicMsg(pData, pSender);
    }   

    @Override
    public IActorRef sender() {
        return msg.sender();
    }

    @Override
    public Exception takeData() {
        return (Exception) msg.takeData();
    }
}
