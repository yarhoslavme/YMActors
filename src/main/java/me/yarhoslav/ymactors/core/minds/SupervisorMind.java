package me.yarhoslav.ymactors.core.minds;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.messages.DeadMsg;

/**
 *
 * @author yarhoslavme
 */
public class SupervisorMind implements IActorMind {
    
    private final SimpleActor owner;
    
    public SupervisorMind(SimpleActor pOwner) {
        owner = pOwner;
    }

    @Override
    public void process() throws Exception {
        if (owner.envelope().message().equals(DeadMsg.INSTANCE)) {
            IActorRef sender = owner.envelope().sender();
            if (sender.addr().equals(owner.addr())) {
                //owner.minions().remove(sender);
            }
        }
    }
    
}
