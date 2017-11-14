package com.yarhoslav.ymactors.core.system;

import com.yarhoslav.ymactors.core.actors.IActorRef;
import com.yarhoslav.ymactors.core.actors.SimpleActor;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public class ActorSystem implements ISystem {

    private final Logger logger = LoggerFactory.getLogger(ActorSystem.class);

    private final String name;
    private final QuantumExecutor quantumsExecutor;
    private final Map<String, IActorRef> actors;

    public ActorSystem(String pName) {
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("ActorSystem's name can't be blank");
        }
        name = pName;
        quantumsExecutor = new QuantumExecutor();
        actors = new ConcurrentHashMap<>();
    }

    @Override
    public boolean requestQuantum(Callable pActor) {
        try {
            quantumsExecutor.submit(pActor);
            return true;
        } catch (RejectedExecutionException | NullPointerException ex) {
            logger.warn("Failed submitting new task to Quantum Executor {}.", pActor, ex);
            return false;
        }

    }

    //ActorSystem API
    public String name() {
        return name;
    }

    //ISystem implementation
    @Override
    public <E extends SimpleActor> IActorRef createActor(E pActorType, String pName) throws IllegalArgumentException {
        if (actors.containsKey(pName)) {
            throw new IllegalArgumentException(String.format("Name:%s already used in System %s", pName, name));
        } else {
            E newActor = pActorType;
            newActor.setName(pName);
            newActor.setSystem(this);
            newActor.start();
            actors.put(pName, newActor);
            return newActor;
        }
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
