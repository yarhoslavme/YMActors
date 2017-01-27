package com.yarhoslav.ymactors.utils;

/**
 *
 * @author Yarhoslav
 */
public class Constants {
    public static final String YMACTORS_VERSION = "0.2-SNAPSHOT";
    
    //Parametros del Loop Principal
    public static final int POOLSIZE = 4;
    public static final int SCHEDULESIZE = 1;
    
    //Direcciones para MsgChannel
    public static final String ADDR_BROADCAST = "BROADCAST_CHANNEL";
    public static final String ADDR_ERROR = "ERROR_CHANNEL";
    public static final String ADDR_DEATH = "DEATH_CHANNEL";
    
    //Mensajes del Systema
    public static final String MSG_SUSCRIBE = "SUSCRIBE";
    public static final String MSG_UNSUSCRIBE = "UNSUSCRIBE";
    public static final String MSG_DEATH = "DEATH_NOTIFICATION";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
