package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.actors.UniverseActor;
import com.yarhoslav.ymactors.core.interfaces.ActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import static java.lang.System.currentTimeMillis;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yarhoslav
 */
public class ActorSystem {

    private final Logger logger = LoggerFactory.getLogger(ActorSystem.class);

    //TODO: Design a better executor service to avoid actors being executed in different threads.
    private final ExecutorService living = new ForkJoinPool();
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1); //TODO: Create APPConfig with external config file
    private final String name;
    private final long startTime = currentTimeMillis();
    private UniverseActor universeActor;

    //TODO: Create own exception classes
    public ActorSystem(String pName) throws IllegalArgumentException {
        if (pName == null) {
            throw new IllegalArgumentException("ActorContainer's name can not be null.");
        }
        name = pName;
    }

    public void start() {
        universeActor = (UniverseActor) ActorsFactory.createActor(UniverseActor.class, UniverseActor.SYSTEMACTOR);
        UniverseContext newContext = new UniverseContext(universeActor, EmptyActor.getInstance(), this);
        universeActor.setContext(newContext);
        universeActor.start();
        isAlive.set(true);
    }

    public IActorContext getContext() {
        return universeActor.getContext();
    }

    public ActorRef findActor(String pName) {
        return universeActor.getContext().findActor(pName);
    }

    public void queueUp(ActorRef pActor) {
        if (isAlive.get()) {
            living.execute(pActor);
        } else {
            logger.warn("System {} is inactive. Actor {} can not be enqueued.", new Object[]{name, pActor.getName()});
        }
    }

    public void ShutDownNow() {
        //TODO: Create Shutdown controlled by UniverseActor -> will send PoisonPill to all Actors in the System and then shuts down the Container.
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

    public ActorRef newActor(Class pActorType, String pName) {
        return universeActor.getContext().newActor(pActorType, pName);
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;
        int i = 0;
        Iterator it = universeActor.getContext().getChildren();
        while (it.hasNext()) {
            i++;
            it.next();
        }

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " Actores:" + i + " Executor service:" + living.toString();

        return tmp;
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }

    public void tell(Object pData, ActorRef pSender) {
        universeActor.tell(pData, pSender);
    }

}
