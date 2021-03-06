package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.actors.minions.IMinions;
import me.yarhoslav.ymactors.core.actors.minions.SimpleMinions;
import me.yarhoslav.ymactors.core.messages.*;
import me.yarhoslav.ymactors.core.minds.IActorMind;
import me.yarhoslav.ymactors.core.minds.InternalActorMind;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.minds.SupervisorMind;
import me.yarhoslav.ymactors.core.services.BroadcastService;
import me.yarhoslav.ymactors.core.system.IActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
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
    private final IActorRef parent;
    private final IMinions minions;
    private final IActorSystem system;
    private final IActorMind internalMind;
    private final SimpleExternalActorMind externalMind;
    private final IActorMind supervisorMind;
    private final AtomicInteger internalStatus;
    private final Worker worker;
    private final int dispatcher;
    private IEnvelope actualEnvelope;

    public <E extends SimpleExternalActorMind> SimpleActor(String pName, IActorRef pParent, IActorSystem pSystem, E pExternalMind) throws IllegalArgumentException {
        //TODO: Move Mailbox creation out of the actor to allow user changes the type of mailbox.
        name = pName;
        parent = pParent;
        addr = parent.addr() + "/" + name;
        system = pSystem;
        internalMind = new InternalActorMind(this);
        supervisorMind = new SupervisorMind(this);
        externalMind = pExternalMind;
        minions = new SimpleMinions(this, system);
        internalStatus = new AtomicInteger(ALIVE);
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
            logger.warn("An exception occurs stopping actor {}.  Exception was ignored.", name, e);
            //TODO: Handle errors.  Put Actor in ERROR internalStatus.  Trigger ERROR procedures.
        } finally {
            worker.stop();
            parent.tell(new HighPriorityEnvelope(DeadMsg.INSTANCE, this));

            BroadcastService broadcast = new BroadcastService(minions.all());
            broadcast.send(new HighPriorityEnvelope(PoisonPill.INSTANCE, this));

            //minions.removeAll();
            internalStatus.set(DEAD);
        }
    }

    //IActorContext Interface Impelmentation
    @Override
    public String addr() {

        return addr;
    }

    @Override
    public String name() {

        return name;
    }

    @Override
    public IActorRef myself() {

        return this;
    }

    @Override
    public IActorSystem system() {

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
    public <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) {

        return minions.createActor(pMinionMind, pName);
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
        return Objects.equals(this.addr, other.addr);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.addr);
        return hash;
    }

}
