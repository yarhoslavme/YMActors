package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yarhoslav
 */
public class ActorsFactory {
    public static BaseActor createActor(Class pActorType, String pName) {
        try {
            //TODO: If name is null then generate a new name
            //TODO: Allow constructor with parameters
            Constructor constructor = pActorType.getConstructor();
            try {
                BaseActor newActor = (BaseActor) constructor.newInstance();
                newActor.setName(pName);
                return newActor;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ActorsFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ActorsFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
