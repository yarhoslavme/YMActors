package me.yarhoslav.ymactors.core.actors;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public class Worker implements IWorker {

    public static final int IDLE = 0;
    public static final int WAITING = 1;
    public static final int RUNNING = 2;
    public static final int STOPPING = 3;
    public static final int DEAD = 4;
    public static final int ERROR = -1;

    private final Queue<IEnvelope> mailbox;
    private final AtomicInteger status;

    public Worker() {
        mailbox = new PriorityBlockingQueue<>();
        status = new AtomicInteger(IDLE);
    }

    private void setStatusWaiting() {
        status.set(WAITING);
        requestQuantum();
    }

    private void setStatusStopping() {
        status.set(STOPPING);
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
            //TODO: Submit task
            setStatusWaiting();
        }
    }

    private void requestQuantum() {
        //TODO: Check error
        if (!hasQuantum.get()) {
            boolean quantumAccepted = system.requestQuantum(this);
            hasQuantum.set(quantumAccepted);
            if (!quantumAccepted) {
                internalErrorHandler(new IllegalStateException(String.format("Quantum executor system %s has denied allocation of a new task for Actor %s", system.name(), id)));
            }
        }
    }

    @Override
    public void stop() {
        mailbox.clear();
        status.set(DEAD);
    }

    @Override
    public void taskDone() {
        setStatusStopping();
    }

    @Override
    public void execute() {
        setStatusRunnig();
        actualEnvelope = mailbox.poll();

        runningState();

        int actualStatus = internalStatus.get();
        if ((actualStatus == RUNNING) || (actualStatus == CLOSING)) {
            hasQuantum.set(false);
    }

}
