package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.BroadcastActor;
import com.yarhoslav.ymactors.utils.Constants;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.messages.PoisonPill;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author YarhoslavME
 */
public final class ActorsContainer {

    private static final Logger LOGGER = getLogger(ActorsContainer.class.getName());
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final ExecutorService workpool = new ForkJoinPool();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(Constants.SCHEDULESIZE);
    private final ConcurrentHashMap<String, IActorRef> actors = new ConcurrentHashMap<>();
    private final long startTime = currentTimeMillis();
    private final String name;
    private final ConcurrentHashMap<String, Object> context = new ConcurrentHashMap<>();

    public ActorsContainer(String pName) throws IllegalArgumentException {
        if (pName != null) {
            name = pName;
        } else {
            throw new IllegalArgumentException("Name is null.");
        }
    }

    public void start() {
        LOGGER.log(Level.INFO, "Starting Actor Container {0}.", name);
        isAlive.set(true);

        //Container Context
        //Broadcast channel
        IActorRef tmpActor = new ActorFactory().createActor(Constants.ADDR_BROADCAST, new BroadcastActor(), this);
        tmpActor.start();
        actors.put(Constants.ADDR_BROADCAST, tmpActor);
        context.put(Constants.ADDR_BROADCAST, tmpActor);

        //Error channel
        tmpActor = createActor(Constants.ADDR_ERROR, new BroadcastActor());
        actors.put(Constants.ADDR_ERROR, tmpActor);
        context.put(Constants.ADDR_ERROR, tmpActor);

        //Lost messages channel
        tmpActor = createActor(Constants.ADDR_DEATH, new BroadcastActor());
        actors.put(Constants.ADDR_DEATH, tmpActor);
        context.put(Constants.ADDR_DEATH, tmpActor);
    }

    public void ShutDownNow() {
        //TODO: Create Shutdown controlled by SystemActor -> will send PoisonPill to all Actors in the System and then shuts down the Container.
        isAlive.set(false);
        LOGGER.log(Level.INFO, "Actors Container {0} is shutting down.", name);
        scheduler.shutdownNow();
        workpool.shutdown();
        try {
            workpool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.WARNING, "An error occurs trying to shutdown the workpool. {0}.", ex.getMessage());
        } finally {
            workpool.shutdownNow();
        }
    }

    public IActorRef createActor(String pName, IActorHandler pHandler) {
        if (!isAlive.get()) {
            //TODO: Big mistake!.  Don't return NULL - FIX IT!
            LOGGER.log(Level.WARNING, "System {0} is inactive. Actor {1} is not created.", new Object[]{name, pName});
            return null;
        }

        if (actors.containsKey(pName)) {
            LOGGER.log(Level.WARNING, "Actor {0} already exists.", pName);
            return actors.get(pName);
        }

        //TODO: Improve error handling when creating actors!
        IActorRef tempActor;
        try {
            tempActor = new ActorFactory().createActor(pName, pHandler, this);
            tempActor.start();
            actors.put(pName, tempActor);
            IActorRef tmpBroadcast = (IActorRef) getContext().get(Constants.ADDR_BROADCAST);
            tmpBroadcast.tell(Constants.MSG_SUSCRIBE, tempActor);

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Error in System {0} creating Actor {1}: {2}.", new Object[]{name, pName, e});
            tempActor = null;
        }
        return tempActor;
    }

    public void killActor(IActorRef pActor) {
        IActorRef tmpBroadcast = (IActorRef) getContext().get(Constants.ADDR_BROADCAST);
        tmpBroadcast.tell(Constants.MSG_UNSUSCRIBE, pActor);
        actors.remove(pActor.getName());
    }

    public ConcurrentHashMap<String, IActorRef> getActors() {
        return actors;
    }

    public IActorRef findActor(String pName) {
        return actors.get(pName);
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " Actores:" + actors.size() + " Executor service:" + workpool.toString();

        return tmp;
    }

    public ScheduledFuture schedule(long demoraInicial, long periodo, final IActorRef destino, final Object mensaje) {
        return scheduler.scheduleWithFixedDelay(() -> {
            destino.tell(mensaje, null);
        }, demoraInicial, periodo, TimeUnit.MILLISECONDS);
    }

    public ExecutorService getExecutor() {
        return workpool;
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }

    public String getName() {
        return name;
    }

    public ConcurrentHashMap getContext() {
        return context;
    }
    
    public boolean getAlive() {
        return isAlive.get();
    }

}
