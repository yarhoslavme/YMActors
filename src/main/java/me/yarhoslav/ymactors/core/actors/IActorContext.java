package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.actors.minions.IMinions;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.system.ISystem;

/**
 * @author yarhoslavme
 */
public interface IActorContext {

    IActorRef myself();

    ISystem system();

    IActorRef parent();

    IEnvelope envelope();

    String name();

    String address();

    String id();

    IMinions minions();

    int status();

    <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName) throws IllegalArgumentException;

    void think(IEnvelope pEnvelope);

    int dispatcher();

}
