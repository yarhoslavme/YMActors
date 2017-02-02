package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorHandler implements IActorHandler {
    Logger logger = LoggerFactory.getLogger(ActorsUniverse.class);

    private IActorRef mySelf;

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void beforeStop() throws Exception {
    }

    @Override
    public IActorRef getMyself() {
        return mySelf;
    }

    @Override
    public void setMyself(IActorRef pMyself) {
        mySelf = pMyself;
    }

    @Override
    public void handleException(Exception pException, IActorRef pChild) {
        
    }

    @Override
    public void process(Object msj, IActorRef pSender) throws Exception {     
    }

}
