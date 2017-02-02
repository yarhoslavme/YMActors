/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.DefaultActor;
import com.yarhoslav.ymactors.core.DefaultActorContext;
import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class SystemActor implements IActorRef {

    private static final String SYSTEMACTOR = "SYSTEM";
    private IActorRef minion;
    private final IActorContext universeContext;

    public SystemActor(IActorContext pContext) {
        universeContext = pContext;
    }

    @Override
    public void tell(Object pData, IActorRef pSender) {
        minion.tell(pData, pSender);
    }

    @Override
    public IActorRef start() throws IllegalStateException {
        minion = new DefaultActor.ActorBuilder(SYSTEMACTOR).withHandler(new DefaultActorHandler()).withContext(new DefaultActorContext(this)).build().start();
        return this;
    }

    @Override
    public String getName() {
        return SYSTEMACTOR;
    }

    @Override
    public IActorContext getContext() {
        return universeContext;
    }

    @Override
    public boolean isAlive() {
        return minion.isAlive();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
