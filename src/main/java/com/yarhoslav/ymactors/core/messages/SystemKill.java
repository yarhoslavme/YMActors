package com.yarhoslav.ymactors.core.messages;

/**
 *
 * @author YarhoslavME
 */
public final class SystemKill {
    private static final SystemKill singleton = new SystemKill();
    public static SystemKill getInstance() {
        return singleton;
    } 
}
