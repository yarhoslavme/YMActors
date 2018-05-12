package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.actors.minions.IMinions;
import me.yarhoslav.ymactors.core.actors.minions.SimpleMinions;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.minds.InternalActorMind;
import me.yarhoslav.ymactors.core.minds.IActorMind;
import me.yarhoslav.ymactors.core.minds.SupervisorMind;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.messages.NormalPriorityEnvelope;
import me.yarhoslav.ymactors.core.messages.DeadMsg;
import me.yarhoslav.ymactors.core.messages.HighPriorityEnvelope;
import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.services.BroadcastService;
import me.yarhoslav.ymactors.core.system.ISystem;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class SimpleActor implements IActorRef, IActorContext {

    public static final int ALIVE = 0;
    public static final int STARTING = 1;
    public static final int RUNNING = 2;
    public static final int CLOSING = 3;
    public static final int DYING = 5;
    public static final int DEAD = 6;
    public static final int ERROR = -1;

    private final Logger logger = LoggerFactory.getLogger(SimpleActor.class);

    //TODO: Create a Context class
    private final String name;
    private final String addr;
    private final String id;
    private final IActorRef parent;
    private final IMinions minions;
    private final ISystem system;
    private final IActorMind internalMind;
    private final SimpleExternalActorMind externalMind;
    private final IActorMind supervisorMind;
    private IEnvelope actualEnvelope;
    private final AtomicInteger internalStatus;
    private final AtomicBoolean hasQuantum;
    private final Worker worker;
    private final int dispatcher;

    public <E extends SimpleExternalActorMind> SimpleActor(String pName, String pAddr, IActorRef pParent, ISystem pSystem, E pExternalMind) throws IllegalArgumentException {
        //TODO: Check name and addr constraints and throws Exception
        //TODO: Move Mailbox creation out of the actor to allow user changes the type of mailbox.
        name = pName;
        addr = pAddr;
        id = addr + "/" + name;
        parent = pParent;
        system = pSystem;
        internalMind = new InternalActorMind(this);
        supervisorMind = new SupervisorMind(this);
        externalMind = pExternalMind;
        minions = new SimpleMinions(this, system);
        internalStatus = new AtomicInteger(ALIVE);
        hasQuantum = new AtomicBoolean(false);
        worker = new Worker(this); //TODO: Context must be a separate object from SimpleActor
        dispatcher = system.getDispatcher();
    }

    //SimpleActor API
    //IActorRef Interface Implementation
    @Override
    public void tell(Object pData, IActorRef pSender) {
        tell(new NormalPriorityEnvelope(pData, pSender));
    }

    @Override
    public void tell(IEnvelope pEnvelope) {
        int actualStatus = internalStatus.get();
        if ((actualStatus == RUNNING) || (actualStatus == CLOSING)) {
            //TODO: Call Worker.
            worker.newMessage(pEnvelope);
        }
    }

    public final void start() {
        internalStatus.set(STARTING);
        try {
            externalMind.initialize(this);
            externalMind.postStart();
            internalStatus.set(RUNNING);
        } catch (Exception e) {
            logger.warn("An exception occurs starting actor {}.  Stoping Actor.", name, e);
            stop();
        }
    }

    public final void stop() {
        internalStatus.set(DYING);
        try {
            externalMind.beforeStop();
        } catch (Exception e) {
            logger.warn("An exception occurs stopping actor {}.  Excetion was ignored.", name, e);
            //TODO: Handle errors.  Put Actor in ERROR internalStatus.  Trigger ERROR procedures.
        } finally {
            worker.stop();
            parent.tell(new HighPriorityEnvelope(DeadMsg.INSTANCE, this));

            BroadcastService broadcast = new BroadcastService(minions.all());
            broadcast.send(new HighPriorityEnvelope(PoisonPill.INSTANCE, this));

            minions.removeAll();
            internalStatus.set(DEAD);
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
    public int status() {
        return internalStatus.get();
    }

    @Override
    public <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName) {
        return minions.add(pMinionMind, pName);
    }
    
    @Override
    public int dispatcher() {
        return dispatcher;
    }

    private void internalErrorHandler(Exception pException) {
        //TODO: Improve error handling.
        internalStatus.set(ERROR);
        externalMind.handleException(pException);
        stop();
    }

    @Override
    public void think(IEnvelope pEnvelope) {
        actualEnvelope = pEnvelope;
        //TODO: Total execution time + Last execution time (En una estructura que devuelve varios valores).  Ver si es interna del SimpleActor.
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
