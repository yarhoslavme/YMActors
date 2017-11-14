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
public abstract class SimpleActor implements IActorRef, Callable {

    private final Logger logger = LoggerFactory.getLogger(SimpleActor.class);

    //TODO: Create a Context class
    private final String name;
    private final String addr;
    private final ISystem system;
    private final IActorMind subconscious;
    private final Queue<IEnvelope> mailbox;
    private IEnvelope actualmsg;
    private final AtomicBoolean hasQuantum;

    public SimpleActor(String pName, String pAddr, ISystem pSystem) {
        //TODO: constructor with Context parameter
        //TODO: Check name and addr constraints
        //TODO: ActorConsciuos => User defined behaviour
        //TODO: Move Mailbox creation out of the actor to allow user changes the type of mailbox.
        name = pName;
        addr = pAddr;
        system = pSystem;
        subconscious = new ActorSubconscious();
        mailbox = new ConcurrentLinkedQueue<>();
        hasQuantum = new AtomicBoolean(false);
    }

    private void requestQuantum() {
        hasQuantum.set(system.requestQuantum(this));
    }

    //SimpleActor API
    abstract public void process() throws Exception;

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
    public String name() {
        return name;
    }

    @Override
    public String address() {
        return addr;
    }

    public IActorRef myself() {
        return this;
    }

    public ISystem system() {
        return system;
    }

    public IEnvelope message() {
        return actualmsg;
    }

    //Callable Interface Implementation
    @Override
    public Object call() throws Exception {
        //TODO: Validate NULL subconcious and NULL conscious?.

        //TODO: Allow multiple messages in the same quantum?
        actualmsg = mailbox.poll();
        if (actualmsg != null) {
            try {
                //TODO: call subconscious only when message from system arrives.
                //TODO: Types of messages: (System, User, Child) - Call different process.
                subconscious.process();
                process();
            } catch (Exception e) {
                logger.warn("An exception occurs processing message {}", name, e);
                //TODO: Handle errors.
            }
        }

        if (!mailbox.isEmpty()) {
            requestQuantum();
        }

        //TODO: Validate the returned value
        //TODO: maybe execution time?. or counter of quantums executed?. or some complex status info?
        return null;
    }

}
