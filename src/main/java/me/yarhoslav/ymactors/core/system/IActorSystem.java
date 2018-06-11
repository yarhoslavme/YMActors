package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yarhoslavme
 */
public interface IActorSystem {

    boolean requestQuantum(int pDispatcher, Runnable pActor);

    String name();

    void shutdown();

    IActorRef findActor(String pId) throws IllegalArgumentException;

    <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException;

    ScheduledFuture schedule(IActorRef pReceiver, IEnvelope pEnvelope, long delay, TimeUnit timeunit);

    ScheduledFuture scheduleAtFixedRate(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);

    ScheduledFuture scheduleWithFixedDelay(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);

    int getDispatcher();
}
