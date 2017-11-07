package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IEnvelope {

    public IActorRef sender();

    public Object message();
}
