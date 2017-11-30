package me.yarhoslav.ymactors.core.messages;

import me.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public class HighPriorityEnvelope implements IEnvelope {
    private final IEnvelope envelope;
    
    public HighPriorityEnvelope(Object pMessage, IActorRef pSender) {
        envelope = new SimpleEnvelope(pMessage, SimpleEnvelope.HIGHPRIORITY, pSender);
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
