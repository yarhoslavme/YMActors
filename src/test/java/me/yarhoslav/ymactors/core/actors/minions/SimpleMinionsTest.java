/*
 * Copyright 2017 yarhoslavme.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.yarhoslav.ymactors.core.actors.minions;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.actors.NullActor;
import me.yarhoslav.ymactors.core.actors.SimpleActor;
import me.yarhoslav.ymactors.core.minds.DumbMind;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import me.yarhoslav.ymactors.core.system.IActorSystem;
import me.yarhoslav.ymactors.core.system.SimpleActorSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author yarhoslavme
 */
public class SimpleMinionsTest {

    private IActorSystem system;
    private SimpleExternalActorMind minionMind;
    private SimpleMinions instance;

    public SimpleMinionsTest() {
    }

    @Before
    public void setUp() {
        system = new SimpleActorSystem("TEST-ACTORSYSTEM");
        minionMind = new DumbMind();
        instance = new SimpleMinions(NullActor.INSTANCE, system);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class SimpleMinions.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        String expResult = "DUMBTEST-ACTOR";

        IActorRef result = instance.add(minionMind, expResult);

        assertTrue(expResult.equals(result.name()));
        assertEquals(instance.count(), 1);
    }

    /**
     * Test of find method, of class SimpleMinions.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        String name = "DUMBTEST-ACTOR";
        IActorRef expResult = instance.add(minionMind, name);
        
        IActorRef result = instance.find(name);

        assertEquals(result, expResult);
    }

    /**
     * Test of summon method, of class SimpleMinions.
     */
    @Test
    public void testSummon() {
        System.out.println("summon");
        String pName = "DUMBTEST-ACTOR";
        IActorRef expResult = instance.add(minionMind, "DUMBTEST-ACTOR");

        SimpleActor result = instance.summon(pName);

        assertEquals(expResult, result);
    }

    /**
     * Test of remove method, of class SimpleMinions.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        IActorRef expResult = instance.add(minionMind, "DUMBTEST-ACTOR");

        IActorRef result = instance.remove(expResult);

        assertEquals(expResult, result);
        assertEquals(instance.count(), 0);
    }

    /**
     * Test of all method, of class SimpleMinions.
     */
    @Test
    public void testAll() {
        System.out.println("all");
        instance.add(minionMind, "DUMBTEST-ACTOR");
        
        Iterator result = instance.all();
        
        assertTrue(result.hasNext());
    }

    /**
     * Test of removeAll method, of class SimpleMinions.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        instance.add(minionMind, "DUMBTEST-ACTOR1");
        instance.add(new DumbMind(), "DUMBTEST-ACTOR2");
        instance.add(new DumbMind(), "DUMBTEST-ACTOR3");
        int expResult = 0;

        instance.removeAll();

        assertEquals(instance.count(), expResult);
    }

    /**
     * Test of count method, of class SimpleMinions.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        instance.add(minionMind, "DUMBTEST-ACTOR1");
        instance.add(new DumbMind(), "DUMBTEST-ACTOR2");
        instance.add(new DumbMind(), "DUMBTEST-ACTOR3");
        int expResult = 3;
        
        int result = instance.count();
        
        assertEquals(expResult, result);
    }

}
