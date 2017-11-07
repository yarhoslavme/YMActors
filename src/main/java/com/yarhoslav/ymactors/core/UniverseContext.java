package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.actors.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorState;
import com.yarhoslav.ymactors.core.interfaces.ISystem;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseContext implements IActorContext {

    Logger logger = LoggerFactory.getLogger(UniverseContext.class);

    private final IActorContext context;

    public UniverseContext(ISystem pSystem) {
        context = new BaseContext(pSystem);
    }

    @Override
    public ISystem getSystem() {
        return context.getSystem();
    }

    @Override
    public void setState(IActorState pState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IActorState getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
