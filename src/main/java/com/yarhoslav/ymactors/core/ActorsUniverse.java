package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.actors.SystemActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import static java.lang.System.currentTimeMillis;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author YarhoslavME
 */
//TODO: Separate the ActorContext from the ActorContainer
public final class ActorsUniverse {

    Logger logger = LoggerFactory.getLogger(ActorsUniverse.class);
    private static final String SYSTEMACTOR = "SYSTEM";
    private final String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final ExecutorService living = new ForkJoinPool();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1); //TODO: Create APPConfig with external config file
    private final long startTime = currentTimeMillis();
    private IActorRef systemActor;

    public ActorsUniverse(String pName) throws IllegalArgumentException {
        if (pName == null) {
            throw new IllegalArgumentException("ActorContainer's name can not be null.");
        }

        name = pName;
    }

    public void start() throws IllegalStateException {
        logger.info("Starting Actor Container {}.", name);
        isAlive.set(true);

        try {
            systemActor = new SystemActor(new UniverseContext(this));
            systemActor.start();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            logger.error("An error occurs setting up the Actor container {}", ex);
            throw new IllegalStateException("An error occurs setting up the Actor container:", ex);
        }
    }

    public void ShutDownNow() {
        //TODO: Create Shutdown controlled by SystemActor -> will send PoisonPill to all Actors in the System and then shuts down the Container.
        isAlive.set(false);
        logger.info("Actors Container {} is shutting down.", name);
        scheduler.shutdownNow();
        living.shutdown();
        try {
            living.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.warn("An error occurs trying to shutdown the workpool.", ex);
        } finally {
            living.shutdownNow();
        }
    }

    public IActorRef createActor(String pName, IActorHandler pHandler) {
        if (!isAlive.get()) {
            logger.warn("System {} is inactive. Actor {} can not be created.", new Object[]{name, pName});
            return EmptyActor.getInstance();
        }

        IActorRef newActor;
        try {
            newActor = systemActor.getContext().createActor(pName, pHandler);
        } catch (IllegalArgumentException e) {
            logger.warn("Error in System {} creating Actor {}: {}.", new Object[]{name, pName, e.getMessage()});
            newActor = EmptyActor.getInstance();
        }
        return newActor;
    }

    public IActorRef findActor(String pName) {
        if (!isAlive.get()) {
            logger.warn("System {} is inactive. There are no actors available.", name);
            return EmptyActor.getInstance();
        }

        if (pName == null) {
            return EmptyActor.getInstance();
        }

        if (pName.startsWith("/")) {
            pName = pName.substring(1);
            String names[] = pName.split("/");
            IActorRef tmpParent = systemActor.getContext().findActor(names[0]);
            if (tmpParent == null) {
                return EmptyActor.getInstance();
            }
            for (int i = 1; i < names.length; i++) {
                IActorRef tmpChild = tmpParent.getContext().findActor(names[i]);
                if (tmpChild == null) {
                    return EmptyActor.getInstance();
                }
                tmpParent = tmpChild;
            }
            return tmpParent;

        } else {
            return EmptyActor.getInstance();
        }
    }

    public void queueUp(IActorRef pActor) {
        if (isAlive.get()) {
            living.execute(pActor);
        } else {
            logger.warn("System {} is inactive. Actor {} can not be enqueued.", new Object[]{name, pActor.getName()});
        }
    }

    public void forgetActor(IActorRef pActor) {
        systemActor.getContext().forgetActor(pActor);
    }

    public ScheduledFuture schedule(long demoraInicial, long periodo, final IActorRef destino, final Object mensaje) {
        return scheduler.scheduleWithFixedDelay(() -> {
            destino.tell(mensaje, null);
        }, demoraInicial, periodo, TimeUnit.MILLISECONDS);
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " Actores:" + systemActor.getContext().getChildren().toString() + " Executor service:" + living.toString();

        return tmp;
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }
    
    public IActorRef getSystemActor() {
        return systemActor;
    }
    
    public void tell(Object pData, IActorRef pSender) {
        systemActor.tell(pData, pSender);
    }

}
