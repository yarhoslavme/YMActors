package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;

/**
 *
 * @author YarhoslavME
 */
public final class PoisonPill implements IActorMsg {
    
    private final String ID = "POISONPILL";
    private static final PoisonPill SINGLETON = new PoisonPill();
    
    public static PoisonPill getInstance() {
        return SINGLETON;
    }  

    @Override
    public IActorRef sender() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object takeData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String id() {
        return ID;
    }
}
