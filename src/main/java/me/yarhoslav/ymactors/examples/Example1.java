package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.IActorSystem;
import me.yarhoslav.ymactors.core.system.SimpleActorSystem;

/**
 *
 * @author manjaro
 */
public class Example1 {
    
    /* Hello world */
    IActorSystem system = new SimpleActorSystem("DEMO");
    IActorRef hello = system.createActor(new HelloWorldMind(), "HELLOWORLD");

    public static void main(String[] args) {
        // TODO code application logic here
        Example1 example = new Example1();
        example.hello.tell("Hello", null);
    }
}
