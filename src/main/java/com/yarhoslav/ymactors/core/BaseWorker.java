package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import com.yarhoslav.ymactors.core.messages.DeathMsg;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public class BaseWorker implements IWorker {

    private final Logger logger = LoggerFactory.getLogger(BaseWorker.class);

    //TODO: Worker keeps Actor's status
    private final IActorContext context;
    private final int dispatcher;
    private final Queue<IActorMsg> mailBox = new ConcurrentLinkedQueue<>();
    private long heartbeats;

    public BaseWorker(IActorContext pContext) {
        context = pContext;
        dispatcher = context.getSystem().getDispatcher();
        heartbeats = 0;
    }

    @Override
    public void process(IActorMsg pMsg, IActorRef pSender) {
        //TODO: Process all internal (system) messages.
    }

    @Override
    public void requestQueue() {
        context.getSystem().queueUp(this);
    }

    @Override
    public int getDispatcher() {
        return dispatcher;
    }

    public void informException(ErrorMsg pMsg) {
        logger.warn("Actor {} throws an exception: ", context.getOwner().getName(), pMsg.takeData());
        //TODO: Should be private method.
        //TODO: Must implement Observable-observer pattern
    }

    @Override
    public void run() {
        if (mailBox.isEmpty()) {
            return;
        }
        heartbeats++;

        Object msg = mailBox.poll();
        if (msg == null) {
            return;
        }
        
        //TODO: Implement State Pattern

        if (msg instanceof IActorMsg) {
            IActorMsg receivedMsg = (IActorMsg) msg;
            Object receivedData = receivedMsg.takeData();
            IActorRef receivedSender = receivedMsg.sender();

            if (receivedData instanceof BroadCastMsg) {
                BroadCastMsg payLoad = (BroadCastMsg) receivedData;
                //broadcast(payLoad);
                receivedData = payLoad.takeData();
                receivedSender = payLoad.sender();
            }
            if (receivedData instanceof PoisonPill) {
                stop();
            } else if (receivedData instanceof ErrorMsg) {
                ErrorMsg payLoad = (ErrorMsg) receivedData;
                ((BaseActor) context.getOwner()).handleException(payLoad.takeData(), payLoad.sender());
            } else if (receivedData instanceof DeathMsg) {
                context.getSystem().removeActor(receivedSender);
            } else {
                try {
                    ((BaseActor) context.getOwner()).process(receivedData, receivedSender);
                } catch (Exception ex) {
                    informException(new ErrorMsg(ex, receivedSender));
                }
            }
        } else {
            informException(new ErrorMsg(new IllegalArgumentException("Message is not IActorMsg type."), context.getOwner()));
        }
    }

    @Override
    public IActorMsg getNextMsg() {
        return mailBox.poll();
    }

    @Override
    public void newMessage(IActorMsg pMsg) {
        if (mailBox.offer(pMsg)) {
            requestQueue();
        }
    }

    public void stop() {
        //TODO: Broadcast PoisonPill to all children.
        //TODO: Implement stopping mechanism 
        //TODO: Remove this line
        //logger.info("{} Stop request received:", name);

        try {
            ((BaseActor) context.getOwner()).beforeStop();
            //broadcast(new BroadCastMsg(PoisonPill.getInstance(), context.getOwner()));
        } catch (Exception ex) {
            informException(new ErrorMsg(ex, context.getOwner()));
        } finally {
            //TODO: Incompatible with UniverseActor.
            //TODO: Borrar esta locura!!!
            //TODO: Implement observable-observer pattern
/*            if (!(context.getParent() instanceof EmptyActor)) {
                logger.info("Actor {} INFORMA DeathMsg ...", context.getOwner().getName());
                context.getParent().tell(DeathMsg.getInstance(), context.getOwner());
            }*/
            context.getSystem().removeActor(context.getOwner());
        }
    }

    /*
    private void broadcast(BroadCastMsg pMsg) {
        BroadcastService broadcast = new BroadcastService(context.getSystem().getActors()).send(pMsg, context.getOwner());
    }
*/
}
