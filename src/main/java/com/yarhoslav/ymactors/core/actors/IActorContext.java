package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
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
    
    public String name();
    
    public String address();
    
    public String id();
    
    public <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName);

}
