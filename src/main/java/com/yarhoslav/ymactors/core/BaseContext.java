package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author yarhoslavme
 */
public final class BaseContext implements IActorContext {
    //TODO: ENUM with states of the actor's context

    Logger logger = LoggerFactory.getLogger(BaseContext.class);

    private final IActorRef owner;
    private final IActorRef parent;
    private final ConcurrentMap<String, IActorRef> children;
    private final ISystem system;

    public BaseContext(IActorRef pOwner, IActorRef pParent, ISystem pSystem) {
        owner = pOwner;
        parent = pParent;
        children = new ConcurrentHashMap<>();
        system = pSystem;
    }

    @Override
    public IActorRef newChild(IActorRef pActorType, String pName) throws IllegalArgumentException, IllegalStateException {
        String path = owner.getPath() + "/" + pName;
        if (children.containsKey(pName)) {
            throw new IllegalArgumentException(String.format("Actor's name already used in system %s", system.getName()));
        } else {
            BaseActor newChild = (BaseActor) pActorType;
            BaseContext newContext = new BaseContext(newChild, owner, system);
            newChild.setContext(newContext);
            newChild.setName(pName);
            newChild.setPath(path);
            newChild.start();
            children.put(pName, newChild);
            return newChild;
        }
    }

    @Override
    public IActorRef findChild(String pName) throws IllegalArgumentException {

        IActorRef tmpActor = children.get(pName);
        if (tmpActor == null) {
            throw new IllegalArgumentException(String.format("Actor named %s doesn't exists in system %s", pName, system.getName()));
        }
        return tmpActor;
    }

    @Override
    public void forgetChild(IActorRef pActor) {
        logger.info("Papa: {} esta removiendo al hijo {}", owner.getName(), children.remove(pActor.getName()).getName());
        logger.info("Papa: {} # de hijos {}", owner.getName(), children.size());
    }

    @Override
    public Iterator getChildren() {
        return children.entrySet().iterator();
    }

    @Override
    public ISystem getSystem() {
        return system;
    }

    @Override
    public IActorRef getParent() {
        return parent;
    }

    @Override
    public IActorRef getChild(String pName) {
        //TODO: Should it throws an exception?
        return children.get(pName);
    }

    @Override
    public IActorRef getOwner() {
        return owner;
    }

}
