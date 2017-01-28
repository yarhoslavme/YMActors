package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorHandler implements IActorHandler {

    private IActorRef mySelf;

    @Override
    public void preStart() {
    }

    @Override
    public void beforeStop() {
    }

    @Override
    public void process(Object msj) {
    }

    @Override
    public IActorRef getMyself() {
        return mySelf;
    }

    @Override
    public void setMyself(IActorRef pMyself) {
        mySelf = pMyself;
    }

}
