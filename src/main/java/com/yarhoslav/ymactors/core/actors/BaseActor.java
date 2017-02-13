package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.BaseWorker;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.messages.BasicMsg;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import com.yarhoslav.ymactors.core.messages.DeathMsg;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import com.yarhoslav.ymactors.core.services.BroadcastService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IWorker;

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

    //TODO: Generate own exception classes
    public BaseActor start() throws IllegalStateException {
        worker = new BaseWorker(context);
        isAlive.set(true);
        try {
            preStart();
        } catch (Exception ex) {
            logger.warn("Actor {} throws an exception in preStart method while created: {}", name, ex.getMessage());
            throw new IllegalStateException("Error starting Actor.", ex);
        }
        return this;
    }





    @Override
    public void tell(Object pData, IActorRef pSender) throws IllegalStateException {
        if (!isAlive.get()) {
            return;
        }
        worker.newMessage(new BasicMsg(pData, pSender));
    }

    public BaseActor self() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setContext(IActorContext pContext) {
        context = pContext;
    }

    public void setName(String pName) {
        name = pName;
    }

}
