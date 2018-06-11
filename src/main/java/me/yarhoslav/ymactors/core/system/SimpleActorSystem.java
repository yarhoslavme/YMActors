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

import java.util.concurrent.*;

/**
 * @author yarhoslavme
 */
public final class SimpleActorSystem implements IActorSystem {

    private final Logger logger = LoggerFactory.getLogger(SimpleActorSystem.class);
    private final String name;
    private final IQuantumExecutor quantumsManager;
    private final ScheduledExecutorService scheduler;  //TODO: Implement separate class to handle Scheduler
    private final SimpleActor userSpace;

    //TODO: Better Name restrictions checking
    public SimpleActorSystem(String pName) {
        if (pName.length() <= 0) {
            throw new IllegalArgumentException("SimpleActorSystem's name can't be blank");
        }
        name = pName;
        quantumsManager = new QuantumExecutor();
        scheduler = new ScheduledThreadPoolExecutor(1);
        userSpace = new SimpleActor("userspace", name + ":/", NullActor.INSTANCE, this, new DumbMind());
        userSpace.start();
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
        return userSpace.createMinion(pMinionMind, pName);
    }

    @Override
    public IActorRef findActor(String pId) throws IllegalArgumentException {
        String tmpId = pId;
        //TODO: Check all rules for Actor's ID.
        if (tmpId.startsWith(name + "://")) {
            tmpId = tmpId.substring(name.length() + ":/".length(), tmpId.length());
        }
        if (tmpId.startsWith("/userspace")) {
            tmpId = tmpId.substring("/userspace".length(), tmpId.length());
        }
        if (tmpId.startsWith("/")) {
            tmpId = tmpId.substring(1, tmpId.length());
            String[] path = tmpId.split("/");
            SimpleActor tmpActor = userSpace.minions().summon(path[0]);
            for (int i = 1; i < path.length; i++) {
                tmpActor = tmpActor.minions().summon(path[i]);
            }
            return tmpActor;
        } else {
            throw new IllegalArgumentException(String.format("Invalid Actor's ID: %s", pId));
        }
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
