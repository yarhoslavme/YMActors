package com.yarhoslav.ymactors.core.system;

/**
 *
 * @author yarhoslavme
 */
public class ActorSystem implements ISystem {
    private final String name;
    private final QuantumExecutor quantumsExecutor;
    
    public ActorSystem(String pName) {
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("ActorSystem's name can't be blank");
        }
        name = pName;
        quantumsExecutor = new QuantumExecutor();
    }

    @Override
    public void requestQuantum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createActor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeActor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void findActor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
