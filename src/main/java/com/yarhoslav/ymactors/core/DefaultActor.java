package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.ICoreMessage;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import com.yarhoslav.ymactors.core.messages.DefaultMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.util.Queue;
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
    private final IActorHandler handler;
    private final IActorContext context;
    private final Queue<ICoreMessage> mailBox = new ConcurrentLinkedQueue<>();
    //TODO: Check whether worth include a systemMailBox
    private IActorRef sender;

    private DefaultActor(ActorBuilder pBuilder) {
        name = pBuilder.name;
        context = pBuilder.context;
        handler = pBuilder.handler;
        sender = null;
        isIdle = new AtomicBoolean(false);
        isAlive = new AtomicBoolean(false);
    }

    public static class ActorBuilder {

        private String name;
        private IActorHandler handler;
        private IActorContext context;

        public ActorBuilder(String pName) throws IllegalArgumentException {
            if (pName == null) {
                throw new IllegalArgumentException("Actor name can not be null.");
            }
            name = pName;
        }

        IActorRef build() {
            IActorRef newActor = new DefaultActor(this);
            handler.setMyself(newActor);
            return newActor;
        }

        public ActorBuilder handler(IActorHandler pHandler) {
            if (pHandler == null) {
                handler = new DefaultActorHandler();
            } else {
                handler = pHandler;
            }
            return this;
        }

        public ActorBuilder context(IActorContext pContext) {
            context = pContext;
            return this;
        }
    }

    @Override
    public IActorRef start() {
        isAlive.set(true);
        isIdle.set(true);

        try {
            handler.preStart();
        } catch (Exception exp) {
            LOGGER.log(Level.WARNING, "Error starting Actor {0}: {1}.", new Object[]{name, exp});
            //TODO: Inform the exception to his parent and let him decide what to do
            this.tell(PoisonPill.getInstance(), this);
            handleException(exp);
        }

        return this;
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
            context.killActor(this);
        }
    }

    private void broadcast(BroadCastMsg pMsg) {
        context.getChildren().entrySet().forEach((entry) -> {
            entry.getValue().tell(pMsg);
        });
    }

    private void handleException(Exception e) {
        //TODO Send the exceptios to his father
        LOGGER.log(Level.WARNING, "Actor {0} throws an exception: {1}", new Object[]{name, e});
    }

    private void requestQueue() {
        if (!isAlive.get()) {
            return;
        }
        if (isIdle.compareAndSet(true, false)) {
            if (context.getContainer().isAlive()) {
                context.getExecutor().execute(this);
            }
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
            sender = _msg.getSender();

            if (_msg instanceof BroadCastMsg) {
                broadcast((BroadCastMsg) _msg);
            }

            if (_msg.getData() instanceof PoisonPill) {
                stop();
                return;
            }

            if (handler != null) {
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
    public IActorRef getSender() {
        return sender;
    }

    @Override
    public boolean isAlive() {
        return isAlive.get();
    }

    @Override
    public boolean isIdle() {
        return isIdle.get();
    }

    @Override
    public IActorContext getContext() {
        return context;
    }

    @Override
    public void tell(Object pData) {
        tell(pData, EmptyActor.getInstance());
    }

    @Override
    public void tell(BroadCastMsg pMsg) {
        if (!isAlive.get()) {
            return;
        }
        if (mailBox.offer(pMsg)) {
            requestQueue();
        }
    }

    @Override
    public void tell(Object pData, IActorRef pSender) {
        if (!isAlive.get()) {
            return;
        }
        if (mailBox.offer(new DefaultMsg(pSender, pData))) {
            requestQueue();
        }
    }

}
