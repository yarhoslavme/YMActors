package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorContext implements IActorContext {

    private final Map<String, IActorRef> children;
    private final IActorRef parent;
    private final IActorContext container;
    //TOOD: Verify whether IActorContext can be used to represent the Container.

    public DefaultActorContext(IActorRef pParent, IActorContext pContainer) {
        children = new HashMap<>();
        parent = pParent;
        container = parent.getContext().getContainer();
    }

    @Override
    public IActorRef createActor(String pName, IActorHandler pHandler) throws IllegalArgumentException {
        //TODO: Name needs to be transformed with parent name.
        IActorContext newContext = new DefaultActorContext(parent, container);
        IActorRef newActor = new DefaultActor.ActorBuilder(pName).handler(pHandler).context(newContext).build().start();
        //TODO: Check what to do whether the name already exists. should it raise an exception?
        children.putIfAbsent(pName, newActor);
        //TODO: Report new actor in Container too.  
        return newActor;
    }

    @Override
    public void killActor(IActorRef pActor) {
        children.remove(pActor.getName());
        //TODO: Kill actor in Container too.
        //TODO: Check lifecicle of actor
    }

    @Override
    public IActorRef findActor(String pName) {
        //TODO: Delegate findactor to Container.
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

}
