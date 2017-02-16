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
public final class BaseContext implements IActorContext {
    //TODO: ENUM with states of the actor's context

    Logger logger = LoggerFactory.getLogger(BaseContext.class);
    private final IActorRef owner;
    private final ISystem system;

    public BaseContext(IActorRef pOwner, ISystem pSystem) {
        owner = pOwner;
        system = pSystem;
    }

    @Override
    public ISystem getSystem() {
        return system;
    }

    @Override
    public IActorRef getOwner() {
        return owner;
    }

}
