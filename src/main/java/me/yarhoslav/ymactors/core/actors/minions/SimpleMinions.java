package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.system.IActorSystem;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author yarhoslavme
 */
public final class SimpleMinions implements IMinions {

    private final Map<String, SimpleActor> minions;
    private final IActorRef parent;
    private final IActorSystem system;

    public SimpleMinions(IActorRef pParent, IActorSystem pSystem) {
        minions = new ConcurrentHashMap<>();
        parent = pParent;
        system = pSystem;
    }

    @Override
    public <E extends SimpleExternalActorMind> IActorRef add(E pMinionMind, String pName) throws IllegalArgumentException {
        SimpleActor tmpActor = new SimpleActor(pName, parent.id(), parent, system, pMinionMind);
        minions.put(tmpActor.name(), tmpActor);
        tmpActor.start();
        return tmpActor;
    }

    @Override
    public IActorRef find(String pName) throws IllegalArgumentException {
        if (!minions.containsKey(pName)) {
            throw new IllegalArgumentException(String.format("Actor with name:%s doesn't exists in Parent %s", pName, parent.id()));
        } else {
            return minions.get(pName);
        }
    }

    @Override
    public SimpleActor summon(String pName) throws IllegalArgumentException {
        if (!minions.containsKey(pName)) {
            throw new IllegalArgumentException(String.format("Actor Id:%s doesn't exists in Parent %s", pName, parent.name()));
        } else {
            return minions.get(pName);
        }
    }

    @Override
    public IActorRef remove(IActorRef pMinion) throws IllegalArgumentException {
        if (!minions.containsKey(pMinion.name())) {
            throw new IllegalArgumentException(String.format("Actor Id:%s doesn't exists in Parent %s", pMinion.name(), parent.name()));
        } else {
            return minions.remove(pMinion.name());
        }
        
    }

    @Override
    public Iterator<SimpleActor> all() {
        return minions.values().iterator();
    }

    @Override
    public void removeAll() {
        minions.clear();
    }

    @Override
    public int count() {
        return minions.size();
    }

}
