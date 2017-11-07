package com.yarhoslav.ymactors.core.actors;

/**
 *
 * @author yarhoslavme
 */
public final class EmptyActor implements IActorRef {

    private static final String EMPTYNAME = "EMPTYACTOR";
    private static final EmptyActor SINGLETON = new EmptyActor();

    public static EmptyActor getInstance() {
        return SINGLETON;
    }

    @Override
    public String getName() {
        return EMPTYNAME;
    }

    @Override
    public void tell(Object pData, IActorRef pSender) {
        throw new IllegalStateException("Empty actor can not receive messages.");
    }

}
