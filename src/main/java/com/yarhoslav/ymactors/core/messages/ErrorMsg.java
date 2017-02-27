package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class ErrorMsg extends BaseMsg {
    
    private final String ID = "ERRORMSG";
    
    public ErrorMsg(Exception pData, IActorRef pSender) {
        super(pData, pSender);
    }   

    @Override
    public String id() {
        return ID;
    }
}
