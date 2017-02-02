package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class EmptyActor implements IActorRef {

    private static final String EMPTYNAME = "EMPTYACTOR";
    private static final EmptyActor SINGLETON = new EmptyActor();
    private IActorContext context;

    public static EmptyActor getInstance() {
        return SINGLETON;
    }

    @Override
    public String getName() {
        return EMPTYNAME;
    }

    @Override
    public IActorContext getContext() {
        throw new IllegalStateException("Empty actor has not context.");
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public IActorRef start() {
        return this;
    }

    @Override
    public void run() {
        throw new IllegalStateException("Empty actor can not enqueued.");
    }

    @Override
    public void tell(Object pData, IActorRef pSender) {
        throw new IllegalStateException("Empty actor can not receive messages.");
    }

}
