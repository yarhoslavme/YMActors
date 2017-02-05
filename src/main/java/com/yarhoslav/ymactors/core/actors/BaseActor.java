package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.messages.BasicMsg;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import com.yarhoslav.ymactors.core.messages.DeathMsg;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseActor implements ActorRef {

    private final Logger logger = LoggerFactory.getLogger(BaseActor.class);
    private IActorContext context;
    private String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);

    private final Queue<IActorMsg> mailBox = new ConcurrentLinkedQueue<>();
    private long heartbeats;
    //TODO: Store the actor class in order to recreate it when restarted.

    public abstract void process(Object pMsg, ActorRef pSender) throws Exception;

    public void preStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Object pData, ActorRef pSender) {
    }

    //TODO: Generate own exception classes
    public BaseActor start() throws IllegalStateException {
        isAlive.set(true);
        heartbeats = 0;
        try {
            preStart();
        } catch (Exception ex) {
            logger.warn("Actor {} throws an exception in preStart method while created: {}", name, ex.getMessage());
            throw new IllegalStateException("Error starting Actor.", ex);
        }
        return this;
    }

    public void stop() {
        //TODO: Broadcast PoisonPill to all children.
        //TODO: Implement stopping mechanism 
        //TODO: Remove this line
        //logger.info("{} Stop request received:", name);
        try {
            beforeStop();
            broadcast(new BroadCastMsg(PoisonPill.getInstance(), this));
        } catch (Exception ex) {
            informException(new ErrorMsg(ex, this));
        } finally {
            isAlive.set(false);
            //TODO: Incompatible with UniverseActor.
            //TODO: Borrar esta locura!!!
            if (!(context.getParent() instanceof EmptyActor)) {
                context.getParent().tell(DeathMsg.getInstance(), this);
            }

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

    public void setContext(IActorContext pContext) {
        context = pContext;
    }

    public void setName(String pName) {
        name = pName;
    }

    public long getHeartbeats() {
        return heartbeats;
    }

    public BaseActor self() {
        return this;
    }

    @Override
    public IActorContext getContext() {
        return context;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void tell(Object pData, ActorRef pSender) {
        if (!isAlive.get()) {
            return;
        }
        if (mailBox.offer(new BasicMsg(pData, pSender))) {
            context.requestQueue();
        }
    }

    @Override
    public void run() {
        if (!isAlive.get()) {
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
                handleException(payLoad.takeData(), payLoad.sender());
            } else if (receivedData instanceof DeathMsg) {
                context.forgetActor(receivedSender);
            } else {
                try {
                    process(receivedData, receivedSender);
                } catch (Exception ex) {
                    informException(new ErrorMsg(ex, receivedSender));
                }
            }
        } else {
            informException(new ErrorMsg(new IllegalArgumentException("Message is not IActorMsg type."), this));
        }
        context.dequeue();
        if (!mailBox.isEmpty()) {
            context.requestQueue();
        }
    }
}
