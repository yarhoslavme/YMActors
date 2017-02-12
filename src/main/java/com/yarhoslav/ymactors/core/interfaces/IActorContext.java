package com.yarhoslav.ymactors.core.interfaces;

import java.util.Iterator;

/**
 *
 * @author yarhoslavme
 */
public interface IActorContext {

    public IActorRef newChild(IActorRef pActor, String pName) throws IllegalArgumentException, IllegalStateException;

    public IActorRef findChild(String pName) throws IllegalArgumentException;

    public void forgetChild(IActorRef pActor);

    public IActorRef getChild(String pName);

    public Iterator getChildren();

    public ISystem getSystem();

    public IActorRef getParent();

    public IActorRef getOwner();



}
