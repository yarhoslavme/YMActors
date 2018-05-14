package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.ActorSystem;
import me.yarhoslav.ymactors.core.system.ISystem;

/**
 *
 * @author manjaro
 */
public class Example2 {
    
    /* Hello world */
    ISystem system = new ActorSystem("DEMO");
    IActorRef hello = system.createActor(new HelloWorld2(), "HELLOWORLD");

    public static void main(String[] args) {
        // TODO code application logic here
        Example2 example = new Example2();
        example.hello.tell("Hello", null);
    }
}
