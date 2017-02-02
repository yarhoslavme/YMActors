package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorContext implements IActorContext {

    private final Map<String, IActorRef> children;
    private final IActorRef parent;
    private final IActorContext container;
    private IActorRef mySelf;
    //TOOD: Verify whether IActorContext can be used to represent the Container.

    public DefaultActorContext(IActorRef pParent) {
        children = new ConcurrentHashMap<>();
        parent = pParent;
        container = parent.getContext().getContainer();
    }

    @Override
    public IActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException, IllegalStateException {
        if (children.containsKey(pName)) {
            return children.get(pName);
        }
        IActorContext newContext = new DefaultActorContext(getMyself());
        IActorRef newActor = new DefaultActor.ActorBuilder(pName).withHandler(pHandler).withContext(newContext).build().start();
        children.put(pName, newActor);
        return newActor;
    }

    @Override
    public IActorRef findActor(String pName) {
        IActorRef tmpActor;
        tmpActor = children.get(pName);
        if (tmpActor == null) {
            tmpActor = EmptyActor.getInstance();
        }
        return tmpActor;
    }

    @Override
    public IActorContext getContainer() {
        return container;
    }

    @Override
    public IActorRef getParent() {
        return parent;
    }

    @Override
    public void forgetActor(IActorRef pActor) {
        children.remove(pActor.getName());
    }

    @Override
    public Iterator getChildren() {
        return children.entrySet().iterator();
    }

    @Override
    public void queueUp(IActorRef pActor) {
        //TODO: Posibilidad de asignar otro executorservice.
        container.queueUp(pActor);
    }

    @Override
    public IActorRef getMyself() {
        return mySelf;
    }

    @Override
    public void setMyself(IActorRef pActor) {
        mySelf = pActor;
    }

}
