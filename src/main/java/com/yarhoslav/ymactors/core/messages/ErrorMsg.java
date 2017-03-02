package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;

/**
 *
 * @author YarhoslavME
 */
public final class ErrorMsg implements IActorMsg {

    private final String ID = "ERRORMSG";
    private final IActorMsg msg;

    public ErrorMsg(Exception pData) {
        msg = new BaseMsg(pData, ID);
    }

    @Override
    public String id() {
        return msg.id();
    }

    @Override
    public Object takeData() {
        return msg.takeData();
    }
}
