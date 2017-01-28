package com.yarhoslav.ymactors.core;

import static com.yarhoslav.ymactors.core.DefaultActor.LOGGER;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author yarhoslavme
 */
public class DefaultActorHandler implements IActorHandler {

    static final Logger LOGGER = getLogger(DefaultActor.class.getName());

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
