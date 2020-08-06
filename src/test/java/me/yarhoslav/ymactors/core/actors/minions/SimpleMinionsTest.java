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

        //IActorRef result = instance.add(minionMind, expResult);

        //assertTrue(expResult.equals(result.name()));
        //assertEquals(instance.count(), 1);
    }
}
