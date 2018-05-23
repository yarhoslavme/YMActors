package me.yarhoslav.ymactors.core.minds;

import me.yarhoslav.ymactors.core.actors.IActorContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author yarhoslavme
 */
public abstract class SimpleExternalActorMind implements IActorMind {

    private IActorContext owner;
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

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

    public void postStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Exception e) {
    }
}
