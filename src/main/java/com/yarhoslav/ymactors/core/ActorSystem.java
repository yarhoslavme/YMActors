package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BaseActor;
import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.actors.UniverseActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.core.services.YMExecutorService;
import static java.lang.System.currentTimeMillis;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.ISystem;
import com.yarhoslav.ymactors.core.interfaces.IWorker;

/**
 *
 * @author yarhoslav
 */
public class ActorSystem implements ISystem {

    private final Logger logger = LoggerFactory.getLogger(ActorSystem.class);

    //TODO: Design a better executor service to avoid actors being executed in different threads.
    //private final ExecutorService living = new ForkJoinPool();
    private final YMExecutorService living = new YMExecutorService(8);
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1); //TODO: Create APPConfig with external config file
    private final String name;
    private final long startTime = currentTimeMillis();
    private UniverseActor universeActor;
    private IActorContext universeContext; 

    //TODO: Create own exception classes
    public ActorSystem(String pName) throws IllegalArgumentException {
        if (pName == null) {
            throw new IllegalArgumentException("ActorContainer's name can not be null.");
        }
        name = pName;
    }

    public void start() {
        universeActor = new UniverseActor();
        universeContext = new UniverseContext(universeActor, EmptyActor.getInstance(), this);
        universeActor.setName(UniverseActor.SYSTEMACTOR);
        universeActor.setContext(universeContext);
        universeActor.start();
        isAlive.set(true);
    }

    public IActorRef findActor(String pName) {
        return universeContext.findChild(pName);
    }

    @Override
    public void queueUp(IWorker pWorker) {
        if (isAlive.get()) {
            //living.execute(pActor);
            living.offer(pWorker);
        } else {
            logger.warn("System {} is inactive. New worker cannot be enqueued.", name);
        }
    }
    
    @Override
    public int getDispatcher() {
        return living.getDispacher();
    }

    public void ShutDownNow() {
        //TODO: Create Shutdown controlled by UniverseActor -> will send PoisonPill to all Actors in the System and then shuts down the Container.
        isAlive.set(false);
        logger.info("Actors Container {} is shutting down.", name);
        scheduler.shutdownNow();
        /*
        living.shutdown();
        try {
            living.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.warn("An error occurs trying to shutdown the workpool.", ex);
        } finally {
            living.shutdownNow();
        }*/
    }

    public IActorRef newActor(BaseActor pActorType, String pName) {
        return universeContext.newChild(pActorType, pName);
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;
        int i = 0;
        Iterator it = universeContext.getChildren();
        while (it.hasNext()) {
            i++;
            it.next();
        }

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " System Heartbeats:" + universeActor.getHeartbeats();
        tmp = tmp + " Mensajes:" + living.mensajes.get();
        tmp = tmp + " Actores:" + i + " Executor service:" + living.toString();

        return tmp;
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }

    public void tell(Object pData, IActorRef pSender) {
        logger.info("Universe actor recibe un mensaje de: {}", pSender.getName());
        universeActor.tell(pData, pSender);
    }

    @Override
    public String getName() {
        return name;
    }

}
