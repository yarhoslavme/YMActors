package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;

import java.util.concurrent.Callable;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public boolean requestQuantum(Callable pActor);

    public IActorRef addActor(IActorRef pActor) throws IllegalArgumentException;

    public IActorRef removeActor(IActorRef pActor) throws IllegalArgumentException;

    public IActorRef getActor(String pId) throws IllegalArgumentException;
    
    //TODO: Add scheduler API.
}
