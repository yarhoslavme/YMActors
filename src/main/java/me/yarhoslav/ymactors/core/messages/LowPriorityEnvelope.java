package me.yarhoslav.ymactors.core.messages;

import me.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public class LowPriorityEnvelope implements IEnvelope {
    private final IEnvelope envelope;
    
    public LowPriorityEnvelope(Object pMessage, IActorRef pSender) {
        envelope = new SimpleEnvelope(pMessage, SimpleEnvelope.LOWPRIORITY, pSender);
    }

    @Override
    public IActorRef sender() {
        return envelope.sender();
    }

    @Override
    public Object message() {
        return envelope.message();
    }

    @Override
    public int priority() {
        return envelope.priority();
    }

    @Override
    public int compareTo(IEnvelope o) {
        return envelope.compareTo(o);
    }
    
}
