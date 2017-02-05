package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.messages.ErrorMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseActor extends BaseActor {

    public static final String SYSTEMACTOR = "SYSTEM";
    private final Logger logger = LoggerFactory.getLogger(UniverseActor.class);

    public UniverseActor() {
    }

    @Override
    public void process(Object pMsg, ActorRef pSender) throws Exception {
    }

    @Override
    public void informException(ErrorMsg pMsg) {
        logger.warn("Actor {} throws an exception: ", getName(), pMsg.takeData());
        //TODO: Implement Error stream
    }

    @Override
    public void stop() {
        //TODO: Implement stopping mechanism 
        //TODO: UniverseActor has completely different way to die
        super.stop();
    }
}
