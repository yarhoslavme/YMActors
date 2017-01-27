package com.yarhoslav.ymactors.core.messages;

/**
 *
 * @author YarhoslavME
 */
public final class PoisonPill {
    private static final PoisonPill SINGLETON = new PoisonPill();
    public static PoisonPill getInstance() {
        return SINGLETON;
    }  
}
