package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public final class NullActor implements IActorRef {

    public static final NullActor INSTANCE = new NullActor();

    private NullActor() {

    }

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
