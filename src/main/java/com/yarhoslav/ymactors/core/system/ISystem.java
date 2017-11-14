package com.yarhoslav.ymactors.core.system;

import com.yarhoslav.ymactors.core.actors.IActorRef;
import com.yarhoslav.ymactors.core.actors.SimpleActor;
import java.util.concurrent.Callable;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public boolean requestQuantum(Callable pActor);

    public <E extends SimpleActor> IActorRef createActor(E pActorType, String pName) throws IllegalArgumentException;

    public void removeActor();

    public void findActor();
}
