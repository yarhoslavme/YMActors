package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorContext implements IActorContext {

    private final Map<String, ActorRef> children;
    private final ActorRef parent;
    private final IActorContext container;
    private ActorRef mySelf;
    //TOOD: Verify whether IActorContext can be used to represent the Container.

    public DefaultActorContext(ActorRef pParent) {
        children = new ConcurrentHashMap<>();
        parent = pParent;
        container = parent.getContext().getSystem();
    }

    @Override
    public ActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException, IllegalStateException {
        if (children.containsKey(pName)) {
            return children.get(pName);
        }
        IActorContext newContext = new DefaultActorContext(getMyself());
        ActorRef newActor = new DefaultActor.ActorBuilder(pName).withHandler(pHandler).withContext(newContext).build().start();
        children.put(pName, newActor);
        return newActor;
    }

    @Override
    public ActorRef findActor(String pName) {
        ActorRef tmpActor;
        tmpActor = children.get(pName);
        if (tmpActor == null) {
            tmpActor = EmptyActor.getInstance();
        }
        return tmpActor;
    }

    @Override
    public IActorContext getSystem() {
        return container;
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

    @Override
    public void queueUp(ActorRef pActor) {
        //TODO: Posibilidad de asignar otro executorservice.
        container.queueUp(pActor);
    }

    @Override
    public ActorRef getMyself() {
        return mySelf;
    }

    @Override
    public void setMyself(ActorRef pActor) {
        mySelf = pActor;
    }

}
