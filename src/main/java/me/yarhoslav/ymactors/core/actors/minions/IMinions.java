package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.Iterator;

/**
 * @author yarhoslavme
 */
public interface IMinions {

    <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException;

    IActorRef find(String pAddr) throws IllegalArgumentException;

    Iterator<IActorRef> all();
}
