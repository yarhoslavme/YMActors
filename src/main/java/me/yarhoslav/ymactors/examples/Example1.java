package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.system.ActorSystem;
import me.yarhoslav.ymactors.core.system.ISystem;

/**
 *
 * @author manjaro
 */
public class Example1 {
    
    /* Hello world */
    ISystem system = new ActorSystem("DEMO");
    IActorRef hello = system.createActor(new HelloWorld(), "HELLOWORLD");

    public static void main(String[] args) {
        // TODO code application logic here
        Example1 example = new Example1();
        example.hello.tell("Hello", null);
    }
}
