package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.system.ISystem;

import java.util.concurrent.Callable;

/**
 *
 * @author yarhoslavme
 */
public class SimpleActor implements IActorRef, Callable {

    //TODO: Create a Context class
    private final String name;
    private final String addr;
    private final ISystem system;
    private final IActorMind subconscious;

    public SimpleActor(String pName, String pAddr, ISystem pSystem) {
        //TODO: constructor with Context parameter
        //TODO: Check name and addr constraints
        //TODO: ActorConsciuos => User defined behaviour
        name = pName;
        addr = pAddr;
        system = pSystem;
        subconscious = new ActorSubconscious();
    }

    //IActorRef Interface Implementation
    @Override
    public void tell(Object pData, IActorRef pSender) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return addr;
    }

    //Callable Interface Implementation
    @Override
    public Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
