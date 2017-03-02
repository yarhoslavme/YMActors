package com.yarhoslav.ymactors.core.interfaces;

/**
 *
 * @author yarhoslavme
 */
public interface IEnvelope {

    public IActorRef sender();

    public Object message();
}
