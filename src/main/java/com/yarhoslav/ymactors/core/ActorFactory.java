package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public class ActorFactory {

    public IActorRef createActor(String pName, IActorHandler pHandler, ActorsContainer pSystem) throws IllegalArgumentException {
        IActorRef tempActor = new DefaultActor(pName, pHandler, pSystem);
        pHandler.setMyself(tempActor);
        return tempActor;
    }
}
