package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import com.yarhoslav.ymactors.core.messages.DeathMsg;
import com.yarhoslav.ymactors.core.messages.BasicMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import java.util.Iterator;
import java.util.Map;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class DefaultActor implements ActorRef {

    private final Logger logger = LoggerFactory.getLogger(ActorsUniverse.class);
    private final AtomicBoolean isQueued;
    private boolean isAlive;
    private final String name;
    private final IActorHandler handler;
    private final IActorContext context;
    private final Queue<IActorMsg> mailBox = new ConcurrentLinkedQueue<>();
    //TODO: Check whether worth include a systemMailBox
    private long heartbeats;

    private DefaultActor(ActorBuilder pBuilder) {
        name = pBuilder.name;
        context = pBuilder.context;
        handler = pBuilder.handler;
        isQueued = new AtomicBoolean(false);
        isAlive = false;
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

        public ActorRef build() {
            ActorRef newActor = new DefaultActor(this);
            handler.setMyself(newActor);
            return newActor;
        }

        public ActorBuilder withHandler(IActorHandler pHandler) {
            //TODO: throw an exception whether handler is null 
            if (pHandler == null) {
                handler = new DefaultActorHandler();
            } else {
                handler = pHandler;
            }
            return this;
        }

        public ActorBuilder withContext(IActorContext pContext) {
            context = pContext;
            return this;
        }
    }

    @Override
    public ActorRef start() throws IllegalStateException {
        isAlive = true;
        isQueued.set(false);
        heartbeats = 0;
        try {
            handler.preStart();
        } catch (Exception ex) {
            logger.warn("Actor {} throws an exception in preStart method while created: {}", name, ex.getMessage());
            throw new IllegalStateException("Error starting Actor.", ex);
        }
        return this;
    }

    private void stop() {
        isQueued.set(false);
        isAlive = false;
        try {
            handler.beforeStop();
        } catch (Exception ex) {
            informException(new ErrorMsg(ex, this));
        } finally {
            context.getParent().tell(DeathMsg.getInstance(), this);
        }
    }

    private void broadcast(BroadCastMsg pMsg) {
        Iterator entries = context.getChildren();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            ActorRef child = (ActorRef) entry.getValue();
            child.tell(pMsg, this);
        }
    }

    public void informException(ErrorMsg pMsg) {
        logger.warn("Actor {} throws an exception: ", name, pMsg.takeData());
        context.getParent().tell(pMsg, this);
    }

    private void requestQueue() {
        if (!isAlive) {
            return;
        }
        if (isQueued.compareAndSet(false, true)) {
            context.getSystem().queueUp(this);
        }
    }

    @Override
    public void run() {
        if (!isAlive) {
            return;
        }
        heartbeats++;

        Object msg = mailBox.poll();
        if (msg == null) {
            return;
        }

        if (msg instanceof IActorMsg) {
            IActorMsg receivedMsg = (IActorMsg) msg;
            Object receivedData = receivedMsg.takeData();
            ActorRef receivedSender = receivedMsg.sender();

            if (receivedData instanceof BroadCastMsg) {
                BroadCastMsg payLoad = (BroadCastMsg) receivedData;
                broadcast(payLoad);
                receivedData = payLoad.takeData();
                receivedSender = payLoad.sender();
            }
            if (receivedData instanceof PoisonPill) {
                stop();
            } else if (receivedData instanceof ErrorMsg) {
                ErrorMsg payLoad = (ErrorMsg) receivedData;
                handler.handleException(payLoad.takeData(), payLoad.sender());
            } else if (receivedData instanceof DeathMsg) {
                context.forgetActor(receivedSender);
            } else {
                try {
                    handler.process(receivedData, receivedSender);
                } catch (Exception ex) {
                    informException(new ErrorMsg(ex, receivedSender));
                }
            }
        } else {
            informException(new ErrorMsg(new IllegalArgumentException("Message is not IActorMsg type."), this));
        }
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public IActorContext getContext() {
        return context;
    }

    @Override
    public void tell(Object pData, ActorRef pSender) {
        if (!isAlive) {
            return;
        }
        if (mailBox.offer(new BasicMsg(pData, pSender))) {
            requestQueue();
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
