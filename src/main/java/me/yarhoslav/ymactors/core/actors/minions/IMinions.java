package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public interface IMinions {

    public <E extends SimpleExternalActorMind> IActorRef add(E pMinionMind, String pName) throws IllegalArgumentException;

    public IActorRef find(String pId) throws IllegalArgumentException;

    public SimpleActor summon(String pId) throws IllegalArgumentException;

    public IActorRef remove(IActorRef pMinion) throws IllegalArgumentException;

    public Iterator all();

    public void removeAll();
    
    public int count();

}
