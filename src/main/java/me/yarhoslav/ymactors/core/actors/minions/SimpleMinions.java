package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.system.IActorSystem;

import java.util.Iterator;

/**
 * @author yarhoslavme
 */
public final class SimpleMinions implements IMinions {

    private final IActorRef owner;
    private final IActorSystem system;

    public SimpleMinions(IActorRef pOwner, IActorSystem pSystem) {
        owner = pOwner;
        system = pSystem;
    }

    @Override
    public <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException {
        //TODO: Create rule for naming.  Example: Only allow chars 'a'..'z' and numbers '0'..'9'.  Don't allow: /, *, -, +, special chars, etc.
        SimpleActor tmpActor = new SimpleActor(pName, owner, system, pMinionMind);
        tmpActor.start();
        system.addActor(tmpActor);

        return tmpActor;
    }

    @Override
    public IActorRef find(String pName) throws IllegalArgumentException {

        return system.findActor(owner.addr() + "/" + pName);
    }

    @Override
    public Iterator<IActorRef> all() {

        return system.findActors(owner.addr());
    }
}
