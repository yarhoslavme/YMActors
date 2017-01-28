package com.yarhoslav.ymactors.utils;

/**
 *
 * @author Yarhoslav
 */
public class Constants {
    public static final String YMACTORS_VERSION = "0.2-SNAPSHOT";
    
    //Pool config
    public static final int POOLSIZE = 4;
    public static final int SCHEDULESIZE = 1;
    
    //Channels
    public static final String ADDR_BROADCAST = "BROADCAST_CHANNEL";
    public static final String ADDR_ERROR = "ERROR_CHANNEL";
    public static final String ADDR_DEATH = "DEATH_CHANNEL";
    
    //System messages
    public static final String MSG_DEATH = "DEATH_NOTIFICATION";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
