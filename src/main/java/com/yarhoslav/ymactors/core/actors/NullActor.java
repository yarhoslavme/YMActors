package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public class NullActor implements IActorRef {

    @Override
    public void tell(Object pData, IActorRef pSender) {
    }

    @Override
    public void tell(IEnvelope pEnvelope) {
    }

    @Override
    public String name() {
        return "NULLACTOR";
    }

    @Override
    public String address() {
        return "";
    }

    @Override
    public String id() {
        return "";
    }
    
}
