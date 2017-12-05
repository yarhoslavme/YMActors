package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.minds.InternalActorMind;
import me.yarhoslav.ymactors.core.minds.IActorMind;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.messages.NormalPriorityEnvelope;
import me.yarhoslav.ymactors.core.system.ISystem;
import me.yarhoslav.ymactors.core.actors.minions.IMinions;
import me.yarhoslav.ymactors.core.actors.minions.SimpleMinions;

import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;
import me.yarhoslav.ymactors.core.messages.DeadMsg;
import me.yarhoslav.ymactors.core.messages.HighPriorityEnvelope;

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
    private final IMinions minions;

    public <E extends SimpleExternalActorMind> SimpleActor(String pName, String pAddr, IActorRef pParent, ISystem pSystem, E pExternalMind) throws IllegalArgumentException {
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
        externalMind = pExternalMind;
        minions = new SimpleMinions(this, system);
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
        mailbox.offer(new NormalPriorityEnvelope(pData, pSender));
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
            parent.tell(new HighPriorityEnvelope(DeadMsg.INSTANCE, this));
            //TODO: Send PoisonPill to all minions.
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
    public IMinions minions() {
        return minions;
    }

    @Override
    public <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName) {
        return minions.add(pMinionMind, pName);
    }

    //Callable Interface Implementation
    @Override
    public Object call() throws Exception {
        if (isAlive.get()) {
            //TODO: Allow multiple messages in the same quantum?
            actualEnvelope = mailbox.poll();
            if (actualEnvelope != null) {
                try {
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
