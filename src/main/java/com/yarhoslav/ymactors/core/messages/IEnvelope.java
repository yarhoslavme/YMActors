package com.yarhoslav.ymactors.core.messages;

import com.yarhoslav.ymactors.core.actors.IActorRef;

/**
 *
 * @author yarhoslavme
 */
public interface IEnvelope {

    public IActorRef sender();

    public Object message();

    public Object header();

//TODO: Create a Class for Message Header (Type, Date/Time, Priority)

}
