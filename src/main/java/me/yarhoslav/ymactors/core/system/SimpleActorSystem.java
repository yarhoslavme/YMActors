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

import java.util.Iterator;
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

    private final Logger logger = LoggerFactory.getLogger(SimpleActorSystem.class);
    private final String name;
    private final IQuantumExecutor quantumsManager;
    private final ScheduledExecutorService scheduler;  //TODO: Implement separate class to handle Scheduler
    private final SimpleActor userSpace;
    private final ConcurrentNavigableMap<String, IActorRef> population;

    //TODO: Better Name restrictions checking
    public SimpleActorSystem(String pName) {
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("SimpleActorSystem's name can't be blank");
        }
        name = pName;
        quantumsManager = new QuantumExecutor();
        scheduler = new ScheduledThreadPoolExecutor(1);
        userSpace = new SimpleActor("userspace", NullActor.INSTANCE, this, new DumbMind());
        userSpace.start();
        population = new ConcurrentSkipListMap<>();
    }

    //SimpleActorSystem API
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

    @Override
    public String name() {
        return name;
    }

    @Override
    public void shutdown() {
        userSpace.tell(PoisonPill.INSTANCE, NullActor.INSTANCE);
        //TODO: While a few seconds before force shutdown.
        //quantumsExecutor.awaitQuiescence(10, TimeUnit.SECONDS);     
        quantumsManager.shutdown();
    }

    //IActorSystem implementation
    @Override
    public <E extends SimpleExternalActorMind> IActorRef createActor(E pMinionMind, String pName) throws IllegalArgumentException {
        IActorRef tempActor = userSpace.createActor(pMinionMind, pName);
        return addActor(tempActor);
    }

    @Override
    public IActorRef addActor(IActorRef pActor) throws IllegalArgumentException {
        if (!population.containsKey(pActor.addr())) {
            throw new IllegalArgumentException(String.format("Actor %s already exists in System %s", pActor.addr(), name));
        }

        return population.putIfAbsent(pActor.addr(), pActor);
    }

    @Override
    public IActorRef removeActor(IActorRef pActor) {

        return population.remove(pActor.addr());
    }

    @Override
    public IActorRef findActor(String pAddr) throws IllegalArgumentException {
        if (!population.containsKey(pAddr)) {
            throw new IllegalArgumentException(String.format("Actor %s does not exists in System %s", pAddr, name));
        }

        return population.get(pAddr);
    }

    @Override
    public Iterator<IActorRef> findActors(String pAddr) {


        return population.headMap(pAddr + "/").values().iterator();
    }


    public String estadistica() {
        //TODO: Fix this!!!
        /*
        return "Actores:" + userSpace.minions().count() + ". Workers:" + quantumsExecutor.getPoolSize() + " Pending:" + quantumsExecutor.getQueuedSubmissionCount() + 
                " En cola"
                + ":" + quantumsExecutor.getQueuedTaskCount();
*/
        return "TODO...";
    }

    @Override
    public ScheduledFuture schedule(IActorRef pReceiver, IEnvelope pEnvelope, long delay, TimeUnit timeunit) {
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
