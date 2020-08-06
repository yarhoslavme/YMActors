package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.minds.DumbMind;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleActorSystemTest {
    private IActorSystem system;
    private IActorRef actor1;

    @Before
    public void setUp() throws Exception {
        system = new SimpleActorSystem("TEST");
        actor1 = system.createActor(new DumbMind(), "ACTOR1");
    }

    @Test
    public void createdActorIsAddedToPopulation() {
        IActorRef tmpActor = system.findActor("@/userspace/ACTOR1");

        assertEquals(actor1.addr(), tmpActor.addr());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removedActorIsRemovedFromPopulation() {
        system.removeActor(actor1);
        IActorRef tmpActor = system.findActor("@/userspace/ACTOR1");
        fail(String.format("removeActor didn't remove Actor %s from population.", tmpActor.addr()));
    }


}