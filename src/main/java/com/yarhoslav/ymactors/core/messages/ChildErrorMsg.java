package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class ChildErrorMsg extends DefaultMsg {
    
    public ChildErrorMsg(final IActorRef pSender, final Exception pData) {
        super(pSender, pData);
    }   
}
