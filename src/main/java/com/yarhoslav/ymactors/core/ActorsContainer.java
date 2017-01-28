package com.yarhoslav.ymactors.core;

import com.yarhoslav.ymactors.core.actors.EmptyActor;
import com.yarhoslav.ymactors.core.interfaces.IActorContext;
import com.yarhoslav.ymactors.utils.Constants;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.core.interfaces.IActorHandler;
import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import static java.lang.System.currentTimeMillis;
import java.util.Map;
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
public final class ActorsContainer implements IActorContext {

    private static final Logger LOGGER = getLogger(ActorsContainer.class.getName());
    private static final String SYSTEMACTOR = "SYSTEM";

    private final String name;
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final ExecutorService workpool = new ForkJoinPool();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(Constants.SCHEDULESIZE);
    private final ConcurrentHashMap<String, IActorRef> systemActors = new ConcurrentHashMap<>();
    private final long startTime = currentTimeMillis();
    private IActorRef systemActor;

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

        EmptyActor empty = EmptyActor.getInstance();
        empty.setContext(this);
        IActorContext newContext = new DefaultActorContext(empty, this);
        systemActor = new DefaultActor.ActorBuilder(SYSTEMACTOR).handler(new DefaultActorHandler()).context(newContext).build().start();
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

    @Override
    public IActorRef createActor(String pName, IActorHandler pHandler) {
        if (!isAlive.get()) {
            //TODO: Big mistake!.  Don't return NULL - FIX IT!
            LOGGER.log(Level.WARNING, "System {0} is inactive. Actor {1} is not created.", new Object[]{name, pName});
            return null;
        }

        //TODO: Improve error handling when creating actors!
        IActorRef newActor = null;
        try {
            systemActor.getContext().createActor(pName, pHandler);

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Error in System {0} creating Actor {1}: {2}.", new Object[]{name, pName, e});
        }
        return newActor;

    }

    @Override
    public void killActor(IActorRef pActor) {
        //TODO: Kill actor in Actorscontainer is especial.
        systemActors.remove(pActor.getName());
    }

    @Override
    public IActorRef findActor(String pName) {
        return systemActors.get(pName);
    }

    public synchronized String getEstadistica() {
        String tmp = "Start: " + startTime;

        tmp = tmp + " Up:" + getUpTime();
        tmp = tmp + " Actores:" + systemActors.size() + " Executor service:" + workpool.toString();

        return tmp;
    }

    public ScheduledFuture schedule(long demoraInicial, long periodo, final IActorRef destino, final Object mensaje) {
        return scheduler.scheduleWithFixedDelay(() -> {
            destino.tell(mensaje, null);
        }, demoraInicial, periodo, TimeUnit.MILLISECONDS);
    }

    public long getUpTime() {
        return currentTimeMillis() - startTime;
    }

    public String getName() {
        return name;
    }

    public IActorContext getContext() {
        return this;
    }

    public boolean getAlive() {
        return isAlive.get();
    }

    @Override
    public ActorsContainer getContainer() {
        return this;
    }

    @Override
    public IActorRef getParent() {
        //TODO: check if return empty actor or return /user actor
        return null;
    }

    public ConcurrentHashMap<String, IActorRef> getActors() {
        return systemActors;
    }

    @Override
    public Map<String, IActorRef> getChildren() {
        return systemActors;
    }

    public void broadcast(Object pMsg, IActorRef pSender) {
        systemActor.tell(new BroadCastMsg(pSender, pMsg));
    }

    @Override
    public boolean isAlive() {
        return isAlive.get();
    }

    @Override
    public ExecutorService getExecutor() {
        return workpool;
    }

}
