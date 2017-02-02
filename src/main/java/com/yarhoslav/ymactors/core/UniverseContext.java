package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseContext implements IActorContext {
    Logger logger = LoggerFactory.getLogger(UniverseContext.class);
    
    private final ActorsUniverse universe;
    
    public UniverseContext(ActorsUniverse pUniverse) {
        universe = pUniverse;
    }

    @Override
    public IActorRef createActor(String pName, IActorHandler pHandler) {
        return universe.createActor(pName, pHandler);
    }

    @Override
    public IActorRef findActor(String pName) {
        return universe.findActor(pName);
    }

    @Override
    public void queueUp(IActorRef pActor) {
        universe.queueUp(pActor);
    }

    @Override
    public IActorContext getContainer() {
        return this;
    }

    @Override
    public IActorRef getParent() {
        return EmptyActor.getInstance();
    }

    @Override
    public void forgetActor(IActorRef pActor) {
        universe.forgetActor(pActor);
    }

    @Override
    public Iterator getChildren() {
        return universe.getSystemActor().getContext().getChildren();
    }

    @Override
    public IActorRef getMyself() {
        return universe.getSystemActor();
    }

    @Override
    public void setMyself(IActorRef pActor) {
        
    }
    
}
