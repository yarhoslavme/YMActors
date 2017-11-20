package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.messages.IEnvelope;
import com.yarhoslav.ymactors.core.system.ISystem;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public IActorRef myself();

    public ISystem system();

    public IActorRef parent();

    public IEnvelope envelope();

}
