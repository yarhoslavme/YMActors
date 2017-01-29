package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author yarhoslavme
 */
public final class EmptyActor implements IActorRef {

    private static final String EMPTYNAME = "EMPTY";
    private static final EmptyActor SINGLETON = new EmptyActor();
    private IActorContext context;

    public static EmptyActor getInstance() {
        return SINGLETON;
    }

    public void setContext(IActorContext pContext) {
        context = pContext;
    }

    @Override
    public String getName() {
        return EMPTYNAME;
    }

    @Override
    public IActorContext getContext() {
        return context;
    }

    @Override
    public IActorRef getSender() {
        return this;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public IActorRef start() {
        return this;
    }

    @Override
    public void run() {

    }

    @Override
    public void tell(Object pData) {

    }

    @Override
    public void tell(BroadCastMsg pMsg) {

    }

    @Override
    public void tell(Object pData, IActorRef pSender) {

    }

}
