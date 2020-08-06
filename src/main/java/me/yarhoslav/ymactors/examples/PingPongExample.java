package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.IActorSystem;
import me.yarhoslav.ymactors.core.system.SimpleActorSystem;

/**
 *
 * @author manjaro
 */
public class PingPongExample {

    /* Hello world */
    public static void main(String[] args) {
        IActorSystem system = new SimpleActorSystem("DEMO");
        IActorRef ping = system.createActor(new Ping(), "PING");
        IActorRef pong = system.createActor(new Pong(), "PONG");
        
        PingPongExample example = new PingPongExample();
        ping.tell("ping", pong);
    }
}
