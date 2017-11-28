package me.yarhoslav.ymactors.core.messages;

import me.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public final class SimpleEnvelope implements IEnvelope {
    
    public static final int LOWPRIORITY = -1;
    public static final int NORMALPRIORITY = 0;
    public static final int HIGHPRIORITY = 1;
    public static final int EMERGENCYPRIORITY = 2;

    private final Object message;
    private final IActorRef sender;
    private final int priority;
    //TODO: Fields: Time, Date.  Also: Expire (Date, TIme).

    public SimpleEnvelope(Object pMsg, int pPriority, IActorRef pSender) {
        message = pMsg;
        sender = pSender;
        priority = pPriority;
    }

    @Override
    public IActorRef sender() {
        return sender;
    }

    @Override
    public Object message() {
        return message;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public int compareTo(IEnvelope o) {
        return Integer.compare(this.priority, o.priority());
    }

}
