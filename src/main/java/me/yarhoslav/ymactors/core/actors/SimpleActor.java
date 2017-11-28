package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.minds.InternalActorMind;
import me.yarhoslav.ymactors.core.minds.IActorMind;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.messages.SimpleEnvelope;
import me.yarhoslav.ymactors.core.system.ISystem;
import java.util.Queue;

import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class SimpleActor implements IActorRef, Callable, IActorContext {

    private final Logger logger = LoggerFactory.getLogger(SimpleActor.class);

    //TODO: Create a Context class
    private final String name;
    private final String addr;
    private final String id;
    private final IActorRef parent;
    private final ISystem system;
    private final IActorMind internalMind;
    private final SimpleExternalActorMind externalMind;
    private final Queue<IEnvelope> mailbox;
    private IEnvelope actualEnvelope;
    private final AtomicBoolean hasQuantum;
    private final AtomicBoolean isAlive;

    public SimpleActor(String pName, String pAddr, IActorRef pParent, ISystem pSystem, SimpleExternalActorMind pConsious) throws IllegalArgumentException {
        //TODO: Check name and addr constraints and throws Exception
        //TODO: Move Mailbox creation out of the actor to allow user changes the type of mailbox.
        name = pName;
        addr = pAddr;
        id = addr + "/" + name;
        parent = pParent;
        system = pSystem;
        internalMind = new InternalActorMind(this);
        mailbox = new PriorityBlockingQueue<>();
        hasQuantum = new AtomicBoolean(false);
        isAlive = new AtomicBoolean(false);
        externalMind = pConsious;
    }

    private void requestQuantum() {
        hasQuantum.set(system.requestQuantum(this));
    }

    //SimpleActor API
    //IActorRef Interface Implementation
    @Override
    public void tell(Object pData, IActorRef pSender) {
        if (!isAlive.get()) {
            return;
        }
        mailbox.offer(new SimpleEnvelope(pData, SimpleEnvelope.NORMALPRIORITY, pSender));
        if (!hasQuantum.get()) {
            requestQuantum();
        }
    }

    @Override
    public void tell(IEnvelope pEnvelope) {
        if (!isAlive.get()) {
            return;
        }
        mailbox.offer(pEnvelope);
        if (!hasQuantum.get()) {
            requestQuantum();
        }
    }

    public final void start() {
        isAlive.set(true);
        try {
            externalMind.initialize(this);
            externalMind.postStart();
        } catch (Exception e) {
            logger.warn("An exception occurs starting actor {}.  Excetion was ignored.", name, e);
            //TODO: Handle errors.  Put Actor in ERROR status.  Trigger ERROR procedures.
            stop();
        }
    }

    public final void stop() {
        try {
            externalMind.beforeStop();
        } catch (Exception e) {
            logger.warn("An exception occurs stopping actor {}.  Excetion was ignored.", name, e);
            //TODO: Handle errors.  Put Actor in ERROR status.  Trigger ERROR procedures.
        } finally {
            isAlive.set(false);
            //TODO: send lost messages to System's Dead Messages Collector
            mailbox.clear();
            //TODO: Send PoisonPill to all minions.
            system.removeActor(this);
        }
    }

    //IActorContext Interface Impelmentation
    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String address() {
        return addr;
    }

    @Override
    public IActorRef myself() {
        return this;
    }

    @Override
    public ISystem system() {
        return system;
    }

    @Override
    public IEnvelope envelope() {
        return actualEnvelope;
    }

    @Override
    public IActorRef parent() {
        return parent;
    }

    @Override
    public <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName) {
        SimpleActor tmpActor = new SimpleActor(pName, this.id, this, system, pMinionMind);
        system.addActor(tmpActor);
        tmpActor.start();
        return tmpActor;
    }

    //Callable Interface Implementation
    @Override
    public Object call() throws Exception {
        if (isAlive.get()) {
            //TODO: Allow multiple messages in the same quantum?
            actualEnvelope = mailbox.poll();
            if (actualEnvelope != null) {
                try {
                    //TODO: call internalMind only when message from system arrives.
                    //TODO: Types of messages: (System, User, Child) - Call different process.
                    internalMind.process();
                    externalMind.process();
                } catch (Exception e) {
                    logger.warn("An exception occurs processing message {}.  Excetion was ignored.", name, e);
                    //TODO: Handle errors.  Put Actor in ERROR status.  Trigger ERROR procedures.
                }
            }

            if (!mailbox.isEmpty()) {
                requestQuantum();
            } else {
                hasQuantum.set(false);
            }

            //TODO: Validate the returned value
            //TODO: maybe execution time?. or counter of quantums executed?. or some complex status info?
            return null;
        } else {
            return null;
        }
    }

}
