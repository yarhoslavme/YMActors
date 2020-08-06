package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.IActorSystem;
import me.yarhoslav.ymactors.core.system.SimpleActorSystem;

/**
 *
 * @author manjaro
 */
public class Example2 {
    
    /* Hello world */
    IActorSystem system = new SimpleActorSystem("DEMO");
    IActorRef hello = system.createActor(new HelloWorldMind2(), "HELLOWORLD");

    public static void main(String[] args) {
        // TODO code application logic here
        Example2 example = new Example2();
        example.hello.tell("Hello", null);
    }
}
