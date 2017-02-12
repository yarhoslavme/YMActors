package com.yarhoslav.ymactors.core;

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

/**
 *
 * @author yarhoslavme
 */
public class BaseWorker implements IWorker {

    private final IActorContext context;
    private final int dispatcher;
    private final Queue<IActorMsg> mailBox = new ConcurrentLinkedQueue<>();

    public BaseWorker(IActorContext pContext) {
        context = pContext;
        dispatcher = context.getSystem().getDispatcher();
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

    @Override
    public void run() {
        /*
        logger.info("Actor {} inicia procesamiento de mensaje...", getName());

        if (mailBox.isEmpty()) {
            return;
        }
        heartbeats++;

        Object msg = mailBox.poll();
        if (msg == null) {
            return;
        }

        if (!isAlive.get()) {
            return;
        }

        if (msg instanceof IActorMsg) {
            IActorMsg receivedMsg = (IActorMsg) msg;
            Object receivedData = receivedMsg.takeData();
            IActorRef receivedSender = receivedMsg.sender();

            if (receivedData instanceof BroadCastMsg) {
                logger.info("Actor {} procesando msg->BROADCAST", getName());
                BroadCastMsg payLoad = (BroadCastMsg) receivedData;
                broadcast(payLoad);
                receivedData = payLoad.takeData();
                receivedSender = payLoad.sender();
            }
            if (receivedData instanceof PoisonPill) {
                logger.info("Actor {} procesando msg->POISONPILL", getName());
                stop();
            } else if (receivedData instanceof ErrorMsg) {
                logger.info("Actor {} procesando msg->ErrorMsg", getName());
                ErrorMsg payLoad = (ErrorMsg) receivedData;
                handleException(payLoad.takeData(), payLoad.sender());
            } else if (receivedData instanceof DeathMsg) {
                logger.info("Actor {} procesando msg->DeathMsg", getName());
                context.forgetChild(receivedSender);
            } else {
                try {
                    process(receivedData, receivedSender);
                } catch (Exception ex) {
                    informException(new ErrorMsg(ex, receivedSender));
                }
            }
        } else {
            informException(new ErrorMsg(new IllegalArgumentException("Message is not IActorMsg type."), this));
        }*/
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

}
