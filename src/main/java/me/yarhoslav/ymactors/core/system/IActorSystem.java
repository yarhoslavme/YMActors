package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yarhoslavme
 */
public interface IActorSystem {

    boolean requestQuantum(int pDispatcher, Runnable pActor);

    String name();

    void shutdown();

    IActorRef findActor(String pAddr) throws IllegalArgumentException;

    <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException;

    void addActor(IActorRef pActor) throws IllegalArgumentException;

    void removeActor(IActorRef pActor) throws IllegalArgumentException;;

    ScheduledFuture scheduleOnce(IActorRef pReceiver, IEnvelope pEnvelope, long delay, TimeUnit timeunit);

    ScheduledFuture scheduleAtFixedRate(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);

    ScheduledFuture scheduleWithFixedDelay(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);

    int getDispatcher();
}
