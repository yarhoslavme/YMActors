package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.NullActor;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.messages.IEnvelope;
import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.minds.DumbMind;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yarhoslavme
 */
public final class SimpleActorSystem implements IActorSystem {

    private static final Logger logger = LoggerFactory.getLogger(SimpleActorSystem.class);
    
    private final String fName;
    private final IQuantumExecutor quantumsManager;
    private final ScheduledExecutorService scheduler;  //TODO: Implement separate class to handle Scheduler
    private final SimpleActor userSpace;
    private final ConcurrentNavigableMap<String, IActorRef> population;

    //SimpleActorSystem API

    /**
     * Construct an object of SimpleActorSystem class.
     *
     * Precondition: 'name' shouldn't be Null or empty.
     *
     * @param  pName           the name of the ActorSystem.
     */
    public SimpleActorSystem(String pName) {
        //TODO: Better Name restrictions checking
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("SimpleActorSystem's fName can't be blank");
        }
        fName = pName;
        quantumsManager = new QuantumExecutor();
        scheduler = new ScheduledThreadPoolExecutor(1);
        userSpace = new SimpleActor("userspace", NullActor.INSTANCE, this, new DumbMind());
        userSpace.start();
        population = new ConcurrentSkipListMap<>();
    }

    @Override
    public String name() {

        return fName;
    }

    /**
     * Try to insert a given Runnable into the main scheduler's dispatcher.
     *
     * @param  pDispatcher     Target dispatcher.
     * @param  pActor          Runnable to be queued.
     * @return                 true - if Runnable was successfully queued.
     */
    @Override
    public boolean requestQuantum(int pDispatcher, Runnable pActor) {
        try {
            quantumsManager.submitTask(pDispatcher, pActor);
            return true;
        } catch (RejectedExecutionException | NullPointerException ex) {
            logger.warn("Failed submitting new task to Quantum Executor {}.  Exception ignored.", pActor, ex);
            return false;
        }
    }

    /**
     * Start the shutdown sequence of the ActorSystem.
     */
    @Override
    public void shutdown() {
        userSpace.tell(PoisonPill.INSTANCE, NullActor.INSTANCE);
        //TODO: While a few seconds before force shutdown.
        quantumsManager.shutdown();
    }

    /**
     * Creates a new actor at top hierarchy level on userspace.
     *
     * @param  pMinionMind      External mind for new actor.
     * @param  pName            Name of the new actor.
     * @return                  Reference to the new actor.
     * @throws IllegalArgumentException if 'pName' already exists or it is invalid.
     */
    @Override
    public <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException {

        return userSpace.createActor(pMinionMind, pName);
    }

    /**
     * Add a given actor into ActorSystem's population.
     *
     * @param  pActor       the given actor reference.
     * @throws IllegalArgumentException if 'pActor' already exists.
     */
    @Override
    public void addActor(IActorRef pActor) throws IllegalArgumentException {
        if (population.containsKey(pActor.addr())) {
            throw new IllegalArgumentException(String.format("Actor %s already exists in System %s", pActor.addr(), fName));
        } else {
            //TODO: Catch exceptions and re-throws the meaning ones to be handled outside.
            population.putIfAbsent(pActor.addr(), pActor);
        }
    }

    /**
     * Remove a given actor from ActorSystem's population.
     *
     * @param  pActor       the given actor reference.
     * @throws IllegalArgumentException if 'Actor's name' doesn't exists in the system.
     */
    @Override
    public void removeActor(IActorRef pActor) throws IllegalArgumentException {
        if (!population.containsKey(pActor.addr())) {
            throw new IllegalArgumentException(String.format("There is not Actor %s in System %s", pActor.addr(), fName));
        }
        //TODO: Catch all possible exceptions and re-throws IllegalArgumentException with a useful message
        population.remove(pActor.addr());
    }

    /**
     * Find the Actor with given address from ActorSystem's population.
     *
     * @param  pAddr       the given actor reference.
     * @throws IllegalArgumentException if 'pAddr' doesn't exists in the system.
     */
    @Override
    public IActorRef findActor(String pAddr) throws IllegalArgumentException {
        if (!population.containsKey(pAddr)) {
            throw new IllegalArgumentException(String.format("There is not Actor %s in System %s", pAddr, fName));
        }

        return population.get(pAddr);
    }

    @Override
    public ScheduledFuture scheduleOnce(IActorRef pReceiver, IEnvelope pEnvelope, long delay, TimeUnit timeunit) {
        return scheduler.schedule(new SchedulerTask(pReceiver, pEnvelope),
                delay,
                timeunit);
    }

    @Override
    public ScheduledFuture scheduleAtFixedRate(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit) {
        return scheduler.scheduleAtFixedRate(new SchedulerTask(pReceiver, pEnvelope),
                initialDelay,
                period,
                timeunit);
    }

    @Override
    public ScheduledFuture scheduleWithFixedDelay(IActorRef pReceiver, IEnvelope pEnvelope, long initialDelay, long period, TimeUnit timeunit) {
        return scheduler.scheduleWithFixedDelay(new SchedulerTask(pReceiver, pEnvelope),
                initialDelay,
                period,
                timeunit);
    }

    @Override
    public int getDispatcher() {
        return quantumsManager.getDispatcher();
    }

}
