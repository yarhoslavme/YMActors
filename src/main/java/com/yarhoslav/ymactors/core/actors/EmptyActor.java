/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.Collections;
import java.util.HashMap;

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
    public IActorRef getParent() {
        return this;
    }

    @Override
    public HashMap<String, IActorRef> getChildren() {
        return (HashMap) Collections.<String, IActorRef>emptyMap();
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
    public void tell(Object pData, IActorRef pSender) {

    }

    @Override
    public IActorRef start() {
        return this;
    }

    @Override
    public void run() {

    }

}
