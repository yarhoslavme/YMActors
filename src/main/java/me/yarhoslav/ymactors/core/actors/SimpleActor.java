package me.yarhoslav.ymactors.core.actors;

import java.util.Objects;
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
import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.minds.SupervisorMind;
import me.yarhoslav.ymactors.core.services.BroadcastService;

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
    private final IActorMind supervisorMind;
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
        mailbox = new PriorityBlockingQueue<>();
        hasQuantum = new AtomicBoolean(false);
        isAlive = new AtomicBoolean(false);
        internalMind = new InternalActorMind(this);
        supervisorMind = new SupervisorMind(this);
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
            logger.warn("An exception occurs starting actor {}.  Stoping Actor.", name, e);
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
            mailbox.clear();
            parent.tell(new HighPriorityEnvelope(DeadMsg.INSTANCE, this));

            BroadcastService broadcast = new BroadcastService(minions.all());
            broadcast.send(new HighPriorityEnvelope(PoisonPill.INSTANCE, this));

            minions.removeAll();
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

    private void internalErrorHandler(Exception pException) {
        //TODO: Improve error handling.
        externalMind.handleException(pException);
        stop();
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
                    supervisorMind.process();
                    externalMind.process();
                } catch (Exception e) {
                    logger.warn("An exception occurs processing message {}.  Excetion was ignored.", name, e);
                    internalErrorHandler(e);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleActor other = (SimpleActor) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
