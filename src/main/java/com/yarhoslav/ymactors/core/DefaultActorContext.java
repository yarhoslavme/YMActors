package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

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

    public DefaultActorContext(IActorRef pParent, IActorContext pContainer) {
        children = new ConcurrentHashMap<>();
        parent = pParent;
        container = parent.getContext().getContainer();
    }

    @Override
    public IActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException {
        if (children.containsKey(pName)) return children.get(pName);
        IActorContext newContext = new DefaultActorContext(getMyself(), container);
        IActorRef newActor = new DefaultActor.ActorBuilder(pName).handler(pHandler).context(newContext).build().start();
        children.put(pName, newActor);
        //TODO: Check what to do whether the name already exists. should it raise an exception?
        return newActor;
    }

    @Override
    public IActorRef findActor(String pName) {
        IActorRef tmpActor;
        if (children.containsKey(pName)) {
            tmpActor = children.get(pName);
        } else {
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
    public Map<String, IActorRef> getChildren() {
        return children;
    }

    @Override
    public boolean isAlive() {
        return container.isAlive();
    }

    @Override
    public ExecutorService getExecutor() {
        return container.getExecutor();
    }

    @Override
    public void setMyself(IActorRef pMyself) {
        mySelf = pMyself;
    }

    @Override
    public IActorRef getMyself() {
        return mySelf;
    }

}
