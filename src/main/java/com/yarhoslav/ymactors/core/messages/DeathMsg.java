package com.yarhoslav.ymactors.core.messages;

/**
 *
 * @author YarhoslavME
 */
public final class DeathMsg {
    private static final DeathMsg SINGLETON = new DeathMsg();
    public static DeathMsg getInstance() {
        return SINGLETON;
    }  
}
