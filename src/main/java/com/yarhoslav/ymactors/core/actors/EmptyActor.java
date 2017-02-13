package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class EmptyActor implements ActorRef {

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
    public IActorContext getContext() {
        throw new IllegalStateException("Empty actor has not context.");
    }

    @Override
    public void run() {
        throw new IllegalStateException("Empty actor can not be enqueued.");
    }

    @Override
    public void tell(Object pData, ActorRef pSender) {
        throw new IllegalStateException("Empty actor can not receive messages.");
    }

}
