package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseContext implements IActorContext {

    Logger logger = LoggerFactory.getLogger(UniverseContext.class);

    private final IActorContext context;

    public UniverseContext(IActorRef pOwner, ISystem pSystem) {
        context = new BaseContext(pOwner, pSystem);
    }

    @Override
    public ISystem getSystem() {
        return context.getSystem();
    }

    @Override
    public IActorRef getOwner() {
        return context.getOwner();
    }

}