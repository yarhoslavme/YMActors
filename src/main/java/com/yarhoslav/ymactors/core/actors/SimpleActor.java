package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.messages.IEnvelope;
import com.yarhoslav.ymactors.core.messages.SimpleEnvelope;
import com.yarhoslav.ymactors.core.system.ISystem;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private final IActorMind subconscious;
    private final IActorMind conscious;
    private final Queue<IEnvelope> mailbox;
    private IEnvelope actualEnvelope;
    private final AtomicBoolean hasQuantum;

    public SimpleActor(String pName, String pAddr, IActorRef pParent, ISystem pSystem, IActorMind pConsious) throws IllegalArgumentException {
        //TODO: Check name and addr constraints and throws Exception
        //TODO: Move Mailbox creation out of the actor to allow user changes the type of mailbox.
        name = pName;
        addr = pAddr;
        id = addr + "/" + name;
        parent = pParent;
        system = pSystem;
        subconscious = new ActorSubconscious();
        mailbox = new ConcurrentLinkedQueue<>();
        hasQuantum = new AtomicBoolean(false);
        conscious = pConsious;
    }

    private void requestQuantum() {
        hasQuantum.set(system.requestQuantum(this));
    }

    //SimpleActor API
    //IActorRef Interface Implementation
    @Override
    public void tell(Object pData, IActorRef pSender) {
        //TODO: Validate input data?
        //TODO: Always create USER TYPE ENVELOPE!!!
        mailbox.offer(new SimpleEnvelope(pData, pSender));
        if (!hasQuantum.get()) {
            requestQuantum();
        }
    }

    @Override
    public void tell(IEnvelope pEnvelope) {
        mailbox.offer(pEnvelope);
        if (!hasQuantum.get()) {
            requestQuantum();
        }
    }

    @Override
    public String id() {
        return id;
    }

    public void postStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Object pData, IActorRef pSender) {
    }
    
    public final void start() {
        //TODO: Change status
        //TODO: Call postStart
    }
    
    public final void stop() {
        //TODO: Send PoisonPill to all minions.
        //TODO: Change status
        //TODO: Call beforeStop
        system.removeActor(this);
    }

    //IActorContext Interface Impelmentation
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

    //Callable Interface Implementation
    @Override
    public Object call() throws Exception {
        //TODO: Validate NULL subconcious and NULL conscious?.
        //TODO: Allow multiple messages in the same quantum?
        actualEnvelope = mailbox.poll();
        if (actualEnvelope != null) {
            try {
                //TODO: call subconscious only when message from system arrives.
                //TODO: Types of messages: (System, User, Child) - Call different process.
                subconscious.process();
                conscious.process();
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
    }

}
