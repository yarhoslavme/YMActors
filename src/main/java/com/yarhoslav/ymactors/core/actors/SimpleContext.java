package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.messages.IEnvelope;
import com.yarhoslav.ymactors.core.system.ISystem;

/**
 *
 * @author yarhoslavme
 */
public class SimpleContext implements IActorContext {

    private final SimpleActor owner;

    public SimpleContext(SimpleActor pOwner) {
        owner = pOwner;
    }

    @Override
    public IActorRef myself() {
        return owner;
    }

    @Override
    public ISystem system() {
        return owner.system();
    }

    @Override
    public IActorRef parent() {
        return owner.parent();
    }

    @Override
    public IEnvelope envelope() {
        return owner.envelope();
    }
}
