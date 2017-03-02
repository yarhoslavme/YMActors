package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.BaseContext;
import com.yarhoslav.ymactors.core.BaseWorker;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import com.yarhoslav.ymactors.core.messages.BaseEnvelope;
import com.yarhoslav.ymactors.core.states.RunningState;
import com.yarhoslav.ymactors.core.states.StoppingState;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseActor implements IActorRef {

    private final Logger logger = LoggerFactory.getLogger(BaseActor.class);
    private IActorContext context;
    private IWorker worker;
    private ISystem system;
    private String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);

    //TODO: Store the actor class in order to recreate it when restarted.
    public abstract void process(Object pMsg, IActorRef pSender) throws Exception;

    public void postStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Object pData, IActorRef pSender) {
    }

    public void setSystem(ISystem pSystem) {
        system = pSystem;
    }

    public void setName(String pName) {
        name = pName;
    }

    //TODO: Generate own exception classes
    public void start() throws IllegalStateException {
        context = new BaseContext(system);
        worker = new BaseWorker(context);
        try {
            postStart();
        } catch (Exception ex) {
            logger.warn("Actor {} throws an exception in preStart method while starting: {}", name, ex.getMessage());
            throw new IllegalStateException("Error starting Actor.", ex);
        }
        context.setState(new RunningState(this));
        isAlive.set(true);
    }

    public void stop() throws Exception {
        isAlive.set(false);
        worker.discardMessages();
        try {
            beforeStop();
        } catch (Exception ex) {
            throw new IllegalStateException(String.format("Error stoping Actor %s", name));
        } finally {
            system.removeActor(this);
            context.setState(new StoppingState());
            //TODO: Cancel and remove ticket from YMExecutor
        }
    }

    @Override
    public void tell(Object pData, IActorRef pSender) throws IllegalStateException {
        if (!isAlive.get()) {
            return;
        }
        worker.newMessage(new BaseEnvelope(pData, pSender));
    }

    @Override
    public String getName() {
        return name;
    }
}
