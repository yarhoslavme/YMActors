package com.yarhoslav.ymactors.core.actors;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author yarhoslavme
 */
public abstract class SimpleActorMind implements IActorMind {

    public IActorContext owner;
    public final AtomicBoolean isInitialized = new AtomicBoolean(false);

    public void initialize(IActorContext pOwner) throws IllegalStateException {
        if (isInitialized.getAndSet(true)) {
            throw new IllegalStateException("Actor mind already initilized.");
        } else {
            owner = pOwner;
        }
    }

    public IActorContext context() {
        return owner;
    }
    //TODO: Create a context for the user.
    //TODO: Create a public method initialize.  Can be called only ones

}
