package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author yarhoslavme
 */
public interface ISystem {

    public boolean requestQuantum(Callable pActor);

    public String name();

    public void shutdown();

    public IActorRef findActor(String pId) throws IllegalArgumentException;

    public <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException;

    public ScheduledFuture schedule(IActorRef pReceiver, IEnvelope pEnvelope, long delay, TimeUnit timeunit);

    public ScheduledFuture scheduleAtFixedRate(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);

    public ScheduledFuture scheduleWithFixedDelay(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit);
}
