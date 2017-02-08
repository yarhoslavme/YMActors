/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yarhoslav.ymactors.core.services;

import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import java.util.Iterator;
import java.util.Map;
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
    
    public BroadcastService send(IActorMsg pMsg, ActorRef pSender) {
        while (children.hasNext()) {
            Map.Entry entry = (Map.Entry) children.next();
            ActorRef child = (ActorRef) entry.getValue();
            child.tell(pMsg, pSender);
        }
        return this;
    }

}
