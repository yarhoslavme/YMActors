package me.yarhoslav.ymactors.core.minds;

import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.messages.PoisonPill;

/**
 *
 * @author yarhoslavme
 */
public class InternalActorMind implements IActorMind {
    
    private final SimpleActor owner;
    
    public InternalActorMind(SimpleActor pOwner ) {
        owner = pOwner;
    }

    @Override
    public void process() throws Exception {
        if (owner.envelope().message().equals(PoisonPill.INSTANCE)) owner.stop();
    }
    
}
