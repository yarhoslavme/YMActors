package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorHandler implements IActorHandler {
    Logger logger = LoggerFactory.getLogger(ActorsUniverse.class);

    private ActorRef mySelf;

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void beforeStop() throws Exception {
    }

    @Override
    public ActorRef getMyself() {
        return mySelf;
    }

    @Override
    public void setMyself(ActorRef pMyself) {
        mySelf = pMyself;
    }

    @Override
    public void handleException(Exception pException, ActorRef pChild) {
        
    }

    @Override
    public void process(Object msj, ActorRef pSender) throws Exception {     
    }

}
