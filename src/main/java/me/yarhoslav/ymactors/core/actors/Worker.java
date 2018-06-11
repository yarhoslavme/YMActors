package me.yarhoslav.ymactors.core.actors;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public class Worker implements IWorker {
    
    private final Logger logger = LoggerFactory.getLogger(Worker.class);

    public static final int IDLE = 0;
    public static final int WAITING = 1;
    public static final int RUNNING = 2;
    public static final int STOPPED = 3;
    public static final int DEAD = 4;
    public static final int ERROR = -1;

    private final Queue<IEnvelope> mailbox;
    private final AtomicInteger status;
    private final IActorContext context;

    public Worker(IActorContext pContext) {
        mailbox = new PriorityBlockingQueue<>();
        status = new AtomicInteger(IDLE);
        context = pContext;
    }

    private void setStatusWaiting() {
        status.set(WAITING);
        requestQuantum();
    }

    private void setStatusStopping() {
        status.set(STOPPED);
        if (!mailbox.isEmpty()) {
            setStatusWaiting();
        } else {
            setStatusIdle();
        }
    }

    private void setStatusIdle() {
        status.set(IDLE);
    }

    private void setStatusRunnig() {
        status.set(RUNNING);
    }

    @Override
    public void newMessage(IEnvelope pEnvelope) {
        //TODO: Check for Exceptions in offer
        mailbox.offer(pEnvelope);
        if (status.get() == IDLE) {
            setStatusWaiting();
        }
    }

    private void requestQuantum() {
        //TODO: Check error
        boolean quantumAccepted = context.system().requestQuantum(context.dispatcher(), this);
        if (!quantumAccepted) {
            internalErrorHandler(new IllegalStateException(String.format("Quantum executor system %s has denied allocation of a new task for Actor %s", context.system().name(), context.myself().addr())));
        }
    }

    private void internalErrorHandler(Exception e) {
        logger.warn("Exception was ignored:", e);
    }

    @Override
    public void stop() {
        mailbox.clear();
        status.set(DEAD);
    }

    @Override
    public void run() {
        setStatusRunnig();
        IEnvelope actualEnvelope = mailbox.poll();
        context.think(actualEnvelope);
        setStatusStopping();
    }
}
