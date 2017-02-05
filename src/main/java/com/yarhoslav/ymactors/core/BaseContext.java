package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author yarhoslavme
 */
public final class BaseContext implements IActorContext {

    private final BaseActor owner;
    private final ActorRef parent;
    private final Map<String, BaseActor> children;
    private final ActorSystem system;
    private final AtomicBoolean isQueued;

    public BaseContext(BaseActor pOwner, ActorRef pParent, ActorSystem pSystem) {
        owner = pOwner;
        parent = pParent;
        children = new ConcurrentHashMap<>();
        system = pSystem;
        isQueued = new AtomicBoolean(false);
    }

    @Override
    public void requestQueue() {
        if (isQueued.compareAndSet(false, true)) {
            system.queueUp(owner);
        }
    }
    
    @Override
    public void dequeue() {
        isQueued.set(false);
    }

    @Override
    public ActorRef newActor(Class pActorType, String pName) {
        if (children.containsKey(pName)) {
            //TODO: Raise an exception when name is already used.
            return children.get(pName);
        }
        BaseActor newChild = ActorsFactory.createActor(pActorType, pName);
        BaseContext newContext = new BaseContext(newChild, owner, system);
        newChild.setContext(newContext);
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
        children.remove(pActor.getName());
    }

    @Override
    public Iterator getChildren() {
        return children.entrySet().iterator();
    }
}
