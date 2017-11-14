package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef {

    //TODO: Validate exceptions.
    public void tell(Object pData, IActorRef pSender);

    public void tell(IEnvelope pEnvelope);

    public String name();

    public String address();
}
