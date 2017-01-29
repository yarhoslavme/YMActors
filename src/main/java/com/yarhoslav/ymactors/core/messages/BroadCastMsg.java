package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class BroadCastMsg extends DefaultMsg {
    
    public BroadCastMsg(final IActorRef pSender, final Object pData) {
        super(pSender, pData);
    }   
}
