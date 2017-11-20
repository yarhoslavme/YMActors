package com.yarhoslav.ymactors.core.services;

import com.yarhoslav.ymactors.core.actors.IActorRef;

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

}
