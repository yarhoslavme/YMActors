package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class BaseContext implements IActorContext {

    Logger logger = LoggerFactory.getLogger(BaseContext.class);

    private final BaseActor owner;
    private final ActorRef parent;
    private final Map<String, BaseActor> children;
    private final ActorSystem system;
    private final AtomicBoolean isQueued;
    private final int dispatcher;

    public BaseContext(BaseActor pOwner, ActorRef pParent, ActorSystem pSystem) {
        owner = pOwner;
        parent = pParent;
        children = new ConcurrentHashMap<>();
        system = pSystem;
        isQueued = new AtomicBoolean(false);
        dispatcher = pSystem.getDispatcher();
    }

    @Override
    public void requestQueue() {
        /*if (isQueued.compareAndSet(false, true)) {
            system.queueUp(owner);
        }*/
        system.queueUp(owner);
    }

    @Override
    public void dequeue() {
        isQueued.set(false);
    }

    @Override
    public ActorRef newActor(BaseActor pActorType, String pName) {
        if (children.containsKey(pName)) {
            //TODO: Raise an exception when name is already used.
            return children.get(pName);
        }
        BaseActor newChild = pActorType;
        BaseContext newContext = new BaseContext(newChild, owner, system);
        newChild.setContext(newContext);
        newChild.setName(pName);
        newChild.start();
        children.put(pName, newChild);
        return newChild;
    }

    @Override
    public ActorRef findActor(String pName) {

        ActorRef tmpActor = children.get(pName);
        if (tmpActor == null) {
            //TODO: Should throws and Exception?
            tmpActor = EmptyActor.getInstance();
        }
        return tmpActor;
    }

    @Override
    public ActorSystem getSystem() {
        return system;
    }

    @Override
    public ActorRef getParent() {
        return parent;
    }

    @Override
    public void forgetActor(ActorRef pActor) {
        logger.info("Papa: {} # de hijos {}", owner.getName(), children.size());
        logger.info("Papa: {} esta removiendo al hijo {}", owner.getName(), children.remove(pActor.getName()).getName());
        logger.info("Papa: {} # de hijos {}", owner.getName(), children.size());
    }

    @Override
    public Iterator getChildren() {
        return children.entrySet().iterator();
    }

    @Override
    public int getDispatcher() {
        return dispatcher;
    }
}
