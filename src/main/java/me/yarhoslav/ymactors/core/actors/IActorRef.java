package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 * @author YarhoslavME
 */
public interface IActorRef {

    //TODO: Validate exceptions.
    void tell(Object pData, IActorRef pSender);

    void tell(IEnvelope pEnvelope);

    String name();

    String address();

    String id();
}
