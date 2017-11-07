package com.yarhoslav.ymactors.core.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class UniverseActor extends BaseActor {

    public static final String SYSTEMACTOR = "SYSTEM";
    private final Logger logger = LoggerFactory.getLogger(UniverseActor.class);

    @Override
    public void process(Object pMsg, IActorRef pSender) throws Exception {
    }

}
