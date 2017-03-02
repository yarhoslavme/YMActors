package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;

/**
 *
 * @author YarhoslavME
 */
public final class BaseMsg implements IActorMsg {

    private final Object data;
    private final String id;

    public BaseMsg(Object pData, String pId) {
        data = pData;
        id = pId;
    }

    @Override
    public Object takeData() {
        return this.data;
    }

    @Override
    public String id() {
        return id;
    }

}
