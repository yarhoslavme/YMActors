package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IEnvelope;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
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
    private final Queue<IEnvelope> mailBox = new ConcurrentLinkedQueue<>();
    private long heartbeats;

    public BaseWorker(IActorContext pContext) {
        context = pContext;
        dispatcher = context.getSystem().getDispatcher();
        heartbeats = 0;
    }

    @Override
    public void requestQueue() {
        //TODO: Use ticket system to get only one spot in the queue
        context.getSystem().queueUp(this);
    }

    @Override
    public int getDispatcher() {
        return dispatcher;
    }

    public void informException(ErrorMsg pMsg) {
        //logger.warn("Actor {} throws an exception: ", context.getOwner().getName(), pMsg.takeData());
        //TODO: Should be private method.
        //TODO: Must implement Observable-observer pattern
    }

    @Override
    public void run() {
        if (mailBox.isEmpty()) {
            return;
        }
        heartbeats++;

        IEnvelope envelope = mailBox.poll();
        if (envelope == null) {
            return;
        }

        Object receivedData = envelope.message();
        IActorRef receivedSender = envelope.sender();

        try {
            context.getState().execute(receivedData, receivedSender);
        } catch (Exception ex) {
            informException(new ErrorMsg(ex));
            //TODO: Change to Error State
        }
    }

    @Override
    public void newMessage(IEnvelope pMsg) {
        if (mailBox.offer(pMsg)) {
            requestQueue();
        }
    }

    @Override
    public void discardMessages() {
        mailBox.clear();
    }

}
