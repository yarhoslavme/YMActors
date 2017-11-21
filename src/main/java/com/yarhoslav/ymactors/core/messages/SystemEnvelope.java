package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class SystemEnvelope extends SimpleEnvelope {

    public SystemEnvelope(Object pMsg, IActorRef pSender, Object pHeader) {
        super(pMsg, pSender, pHeader);
    }

}
