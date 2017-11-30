package me.yarhoslav.ymactors.core.messages;

import me.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public class EmergencyPriorityEnvelope implements IEnvelope {
    private final IEnvelope envelope;
    
    public EmergencyPriorityEnvelope(Object pMessage, IActorRef pSender) {
        envelope = new SimpleEnvelope(pMessage, SimpleEnvelope.EMERGENCYPRIORITY, pSender);
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
