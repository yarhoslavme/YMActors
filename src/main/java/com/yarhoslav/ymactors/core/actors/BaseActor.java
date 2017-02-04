package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseActor implements Runnable {
    
    private final Logger logger = LoggerFactory.getLogger(BaseActor.class);
    private final IActorContext context;

    public abstract void process(Object pMsg, ActorRef pSender) throws Exception;
    
    public BaseActor(IActorContext pContext) {
        context = pContext;
    }
    
    
    public IActorContext getContext() {
        return context;
    }
    
}
