package me.yarhoslav.ymactors.core.services;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.messages.IEnvelope;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class BroadcastService {

    private final Logger logger = LoggerFactory.getLogger(BroadcastService.class);
    
    private final Iterator children;
    
    public BroadcastService(Iterator pChildren) {
        children = pChildren;
    }
    
    public BroadcastService send(Object pMsg, IActorRef pSender) {
        while (children.hasNext()) {
            IActorRef child = (IActorRef) children.next();
            child.tell(pMsg, pSender);
        }
        return this;
    }

    public BroadcastService send(IEnvelope pEnvelope) {
        while (children.hasNext()) {
            IActorRef child = (IActorRef) children.next();
            child.tell(pEnvelope);
        }
        return this;
    }
    
}
