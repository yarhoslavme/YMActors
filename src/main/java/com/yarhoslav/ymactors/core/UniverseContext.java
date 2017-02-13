package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
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

    private final IActorContext context;

    public UniverseContext(BaseActor pOwner, ActorRef pParent, ActorSystem pSystem) {
        context = new BaseContext(pOwner, EmptyActor.getInstance(), pSystem);
    }

    @Override
    public ActorRef newActor(BaseActor pActorType, String pName) throws IllegalArgumentException, IllegalStateException {
        return context.newActor(pActorType, pName);
    }

    @Override
    public ActorRef findActor(String pName) {
        if (!pName.startsWith("/")) {
            return context.findActor(pName);
        } else {
            pName = pName.substring(1);
            String names[] = pName.split("/");
            ActorRef tmpParent = context.findActor(names[0]);
            if (tmpParent == null) {
                return EmptyActor.getInstance();
            }
            for (int i = 1; i < names.length; i++) {
                ActorRef tmpChild = tmpParent.getContext().findActor(names[i]);
                if (tmpChild == null) {
                    return EmptyActor.getInstance();
                }
                tmpParent = tmpChild;
            }
            return tmpParent;
        }
    }

    @Override
    public void forgetActor(ActorRef pActor) {
        context.forgetActor(pActor);
    }

    @Override
    public Iterator getChildren() {
        return context.getChildren();
    }

    @Override
    public ActorSystem getSystem() {
        return context.getSystem();
    }

    @Override
    public ActorRef getParent() {
        return context.getParent();
    }

    @Override
    public void requestQueue() {
        context.requestQueue();
    }

    @Override
    public void dequeue() {
        context.dequeue();
    }
}
