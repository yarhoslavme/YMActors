package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.ICoreMessage;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.messages.DefaultMessage;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import com.yarhoslav.ymactors.utils.Constants;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author YarhoslavME
 */
public class DefaultActor implements IActorRef {

    static final Logger LOGGER = getLogger(DefaultActor.class.getName());
    private final AtomicBoolean isIdle;
    private final AtomicBoolean isAlive;
    private final String name;
    private final ActorsContainer system;
    private final Queue<ICoreMessage> mailBox = new ConcurrentLinkedQueue<>();
    private final IActorHandler handler;
    private IActorRef sender;

    public DefaultActor(String pName, IActorHandler pHandler, ActorsContainer pSystem) throws IllegalArgumentException {
        if ((pName != null) && (pSystem != null) && (pHandler != null)) {
            isIdle = new AtomicBoolean(false);
            isAlive = new AtomicBoolean(false);
            name = pName;
            system = pSystem;
            handler = pHandler;
            sender = null;
        } else {
            throw new IllegalArgumentException("Null parameter: (ActorName, ActorHandler, ActorsContainer).");
        }
    }

    @Override
    public void start() {
        isAlive.set(true);
        isIdle.set(true);

        try {
            handler.postStart();
        } catch (Exception exp) {
            LOGGER.log(Level.WARNING, "Error starting Actor {0}: {1}.", new Object[]{name, exp});
            this.tell(PoisonPill.getInstance(), this);
            handleException(exp);
        }
    }

    private void stop() {
        isIdle.set(false);
        isAlive.set(false);
        try {
            handler.beforeStop();
        } catch (Exception exp) {
            LOGGER.log(Level.WARNING, "Error stoping Actor {0}: {1}.", new Object[]{name, exp});
            handleException(exp);
        } finally {
            IActorRef tmpActor = (IActorRef) getContainer().getContext().get(Constants.ADDR_DEATH);
            tmpActor.tell(Constants.MSG_DEATH, this);
            system.killActor(this);
        }
    }

    private void handleException(Exception e) {
        IActorRef tmpActor = (IActorRef) getContainer().getContext().get(Constants.ADDR_ERROR);
        tmpActor.tell(e, this);
        LOGGER.log(Level.WARNING, "Actor {0} throws an exception: {1}", new Object[]{name, e});
    }

    private void requestQueue() {
        if (!isAlive.get()) {
            return;
        }
        if (isIdle.compareAndSet(true, false)) {
            if (system.getAlive()) {
                system.getExecutor().execute(this);
            }
        }
    }

    @Override
    public void tell(Object pData, IActorRef pSender) {
        if (!isAlive.get()) {
            return;
        }
        if (mailBox.offer(new DefaultMessage(pSender, pData))) {
            requestQueue();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        if (!isAlive.get()) {
            return;
        }
        try {
            ICoreMessage _msg = mailBox.poll();
            if (_msg.getData() instanceof PoisonPill) {
                stop();
                return;
            }

            if (handler != null) {
                sender = _msg.getSender();
                handler.process(_msg.getData());
            }
        } catch (Exception e) {
            handleException(e);
        } finally {
            //TODO: Que pasa con los mensajes no procesados?
            isIdle.set(true);
            if (!mailBox.isEmpty()) {
                requestQueue();
            }
        }
    }

    @Override
    public ActorsContainer getContainer() {
        return system;
    }

    @Override
    public IActorRef getSender() {
        return sender;
    }

    @Override
    public ConcurrentHashMap<String, Object> getContext() {
        return system.getContext();
    }

}
