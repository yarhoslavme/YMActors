package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.NullActor;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.DumbMind;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslavme
 */
public final class ActorSystem implements ISystem {

    private final Logger logger = LoggerFactory.getLogger(ActorSystem.class);

    private final String name;
    private final QuantumExecutor quantumsExecutor;
    private final ScheduledExecutorService scheduler;  //TODO: Public methods to schedule a message.
    private final Map<String, IActorRef> actors;
    private final SimpleActor userSpace;

    //TODO: Better Name restrictions checking
    public ActorSystem(String pName) {
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("ActorSystem's name can't be blank");
        }
        name = pName;
        quantumsExecutor = new QuantumExecutor();
        scheduler = new ScheduledThreadPoolExecutor(1);
        actors = new ConcurrentHashMap<>();
        userSpace = new SimpleActor("userspace", name+":/", NullActor.INSTANCE, this, new DumbMind());
    }

    //ActorSystem API
    @Override
    public boolean requestQuantum(Callable pActor) {
        try {
            quantumsExecutor.submit(pActor);
            return true;
        } catch (RejectedExecutionException | NullPointerException ex) {
            logger.warn("Failed submitting new task to Quantum Executor {}.  Exception ignored.", pActor, ex);
            return false;
        }
    }

    public String name() {
        return name;
    }

    public void start() {
        //TODO: Change Status.
    }

    public void shutdown() {
        //TODO: Send PoisonPill to UserSpace and SystemSpace
    }

    //ISystem implementation
    @Override
    public IActorRef addActor(IActorRef pActor) throws IllegalArgumentException {
        if (actors.containsKey(pActor.id())) {
            throw new IllegalArgumentException(String.format("Actor Id:%s already used in System %s", pActor.id(), name));
        } else {
            actors.put(pActor.id(), pActor);
            return pActor;
        }
    }

    @Override
    public IActorRef removeActor(IActorRef pActor) throws IllegalArgumentException {
        if (!actors.containsKey(pActor.id())) {
            throw new IllegalArgumentException(String.format("Actor Id:%s doesn't exists in System %s", pActor.id(), name));
        } else {
            return actors.remove(pActor.id());
        }
    }

    @Override
    public IActorRef findActor(String pId) throws IllegalArgumentException {
        if (!actors.containsKey(pId)) {
            throw new IllegalArgumentException(String.format("Actor Id:%s doesn't exists in System %s", pId, name));
        } else {
            return actors.get(pId);
        }
    }
    
    public <E extends SimpleExternalActorMind> IActorRef createMinion(E pMinionMind, String pName) {
        return userSpace.createMinion(pMinionMind, pName);
    }
    
    public String estadistica() {
        return "Actores:" + actors.size()+". Forkjoint:"+quantumsExecutor.toString();
    }

}
