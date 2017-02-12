package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseContext implements IActorContext {

    Logger logger = LoggerFactory.getLogger(UniverseContext.class);

    private final IActorContext context;

    public UniverseContext(BaseActor pOwner, IActorRef pParent, ActorSystem pSystem) {
        context = new BaseContext(pOwner, EmptyActor.getInstance(), pSystem);
    }

    @Override
    public IActorRef newChild(IActorRef pActorType, String pName) throws IllegalArgumentException, IllegalStateException {
        return context.newChild(pActorType, pName);
    }

    @Override
    public IActorRef findChild(String pName) throws IllegalArgumentException {
        /*
        if (!pName.startsWith("/")) {
            return context.findChild(pName);
        } else {
            pName = pName.substring(1);
            String names[] = pName.split("/");
            IActorContext tmpContext = context;
            IActorRef tmpParent = null;
            for (String name : names) {
                tmpParent = tmpContext.findChild(name);
                if (tmpParent == null) {
                    throw new IllegalArgumentException(String.format("Actor named %s doesn't exists in system %s", pName, context.getSystem().getName()));
                }
                //TODO: improve this
                tmpContext = ((BaseActor) tmpParent).getContext();
            }
            return tmpParent;
        }*/
        return null;
    }

    @Override
    public void forgetChild(IActorRef pActor) {
        context.forgetChild(pActor);
    }

    @Override
    public Iterator getChildren() {
        return context.getChildren();
    }

    @Override
    public IActorRef getChild(String pName) {
        return context.getChild(pName);
    }

    @Override
    public ISystem getSystem() {
        return context.getSystem();
    }

    @Override
    public IActorRef getParent() {
        return context.getParent();
    }

    @Override
    public IActorRef getOwner() {
        return context.getOwner();
    }

}
