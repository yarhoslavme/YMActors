package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

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
    public ActorRef createActor(String pName, IActorHandler pHandler) {
        return universe.createActor(pName, pHandler);
    }

    @Override
    public ActorRef findActor(String pName) {
        return universe.findActor(pName);
    }

    @Override
    public void queueUp(ActorRef pActor) {
        universe.queueUp(pActor);
    }

    @Override
    public IActorContext getSystem() {
        return this;
    }

    @Override
    public ActorRef getParent() {
        return EmptyActor.getInstance();
    }

    @Override
    public void forgetActor(ActorRef pActor) {
        universe.forgetActor(pActor);
    }

    @Override
    public Iterator getChildren() {
        return universe.getSystemActor().getContext().getChildren();
    }

    @Override
    public ActorRef getMyself() {
        return universe.getSystemActor();
    }

    @Override
    public void setMyself(ActorRef pActor) {
        
    }
    
}
