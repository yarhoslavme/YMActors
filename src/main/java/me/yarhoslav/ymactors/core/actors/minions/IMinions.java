package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.Iterator;

/**
 * @author yarhoslavme
 */
public interface IMinions {

    <E extends SimpleExternalActorMind> IActorRef add(E pMinionMind, String pName) throws IllegalArgumentException;

    IActorRef find(String pId) throws IllegalArgumentException;

    SimpleActor summon(String pId) throws IllegalArgumentException;

    IActorRef remove(IActorRef pMinion) throws IllegalArgumentException;

    Iterator<SimpleActor> all();

    void removeAll();

    int count();

}
