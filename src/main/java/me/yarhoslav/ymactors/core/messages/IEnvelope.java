package me.yarhoslav.ymactors.core.messages;

import me.yarhoslav.ymactors.core.actors.IActorRef;

/**
 * @author yarhoslavme
 */
public interface IEnvelope extends Comparable<IEnvelope> {

    IActorRef sender();

    Object message();

    int priority();

//TODO: Create a Class for Message Header (Type, Date/Time, Priority)

}
