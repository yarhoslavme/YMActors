package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.BaseWorker;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import com.yarhoslav.ymactors.core.messages.BaseEnvelope;

/**
 *
 * @author yarhoslavme
 */
public abstract class BaseActor implements IActorRef {

    private final Logger logger = LoggerFactory.getLogger(BaseActor.class);
    private IActorContext context;
    private IWorker worker;
    private String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);

    //TODO: Store the actor class in order to recreate it when restarted.
    public abstract void process(Object pMsg, IActorRef pSender) throws Exception;

    public void preStart() throws Exception {
    }

    public void beforeStop() throws Exception {
    }

    public void handleException(Object pData, IActorRef pSender) {
    }

    public void setContext(IActorContext pContext) {
        context = pContext;
    }

    public void setName(String pName) {
        name = pName;
    }

    //TODO: Generate own exception classes
    public BaseActor start() throws IllegalStateException {
        worker = new BaseWorker(context);
        isAlive.set(true);
        try {
            preStart();
        } catch (Exception ex) {
            logger.warn("Actor {} throws an exception in preStart method while starting: {}", name, ex.getMessage());
            throw new IllegalStateException("Error starting Actor.", ex);
        }
        return this;
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
