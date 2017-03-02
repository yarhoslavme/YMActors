package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.interfaces.IActorMsg;

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
    public Object takeData() {
        return SINGLETON;
    }

    @Override
    public String id() {
        return ID;
    }
}
