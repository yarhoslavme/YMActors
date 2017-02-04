package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class BroadCastMsg implements IActorMsg {

    private final BasicMsg msg;

    public BroadCastMsg(final Exception pData, final ActorRef pSender) {
        msg = new BasicMsg(pData, pSender);
    }

    @Override
    public ActorRef sender() {
        return msg.sender();
    }

    @Override
    public Exception takeData() {
        return (Exception) msg.takeData();
    }
}
