package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.ActorSystem;
import me.yarhoslav.ymactors.core.system.ISystem;

/**
 *
 * @author manjaro
 */
public class PingPongExample {

    /* Hello world */
    public static void main(String[] args) {
        ISystem system = new ActorSystem("DEMO");
        IActorRef ping = system.createActor(new Ping(), "PING");
        IActorRef pong = system.createActor(new Pong(), "PONG");
        
        PingPongExample example = new PingPongExample();
        ping.tell("ping", pong);
    }
}
