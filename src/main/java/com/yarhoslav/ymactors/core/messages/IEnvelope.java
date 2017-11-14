package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IEnvelope {
    //TODO: Envelope could have other propierties like date-time of message (timeout), type of message (system, user, child, urgent, etc).

    public IActorRef sender();

    public Object message();
}
