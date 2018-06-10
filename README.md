# YMActors

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.yarhoslav/YMActors/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.yarhoslav/YMActors)
[![GitHub version](https://badge.fury.io/gh/yarhoslavme%2FYMActors.svg)](https://badge.fury.io/gh/yarhoslavme%2FYMActors)
[![Travis CI](https://travis-ci.org/yarhoslavme/YMActors.svg?branch=master)](https://travis-ci.org/yarhoslavme/YMActors)
[![Coverage Status](https://coveralls.io/repos/github/yarhoslavme/YMActors/badge.svg?branch=master)](https://coveralls.io/github/yarhoslavme/YMActors?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fb4336d954a046859cf8df34f738a52a)](https://www.codacy.com/app/yarhoslavme/YMActors?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yarhoslavme/YMActors&amp;utm_campaign=Badge_Grade)
[![Join the chat at https://gitter.im/YMActors/Lobby](https://badges.gitter.im/YMActors/Lobby.svg)](https://gitter.im/YMActors/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Actors framework for Java

**Goals:**

>1. Create a pure/lightweight Java actor framework, easy to use.
>2. Inspired by AKKA (See [Akka.io](http://akka.io)) but written from scratch.

The YMActors project gives you all tools you need to write highly resilient/elastic/multithreaded systems, easy and without all the headaches of multithreading programming.  Please, refer to project's documentation for further information.

### Examples

#### Simple "Hello World" example. 

First, create one actor HelloWorldMind that can accept two messages ("Hello" and "World") and will respond accordingly.

```java
public class HelloWorldMind extends SimpleExternalActorMind {
    private final Logger logger = LoggerFactory.getLogger(HelloWorldMind.class);

    @Override
    public void process() throws Exception {
        String msg = (String) context().envelope().message();
        logger.info("Mensaje {}", msg);

        if (msg.equals("Hello")) {
            context().myself().tell("World", context().myself());
        }
        if (msg.equals("World")) {
            context().myself().tell("Hello", context().myself());
        }
    }
}
```

Deeper look into the code:

SimpleExternalActorMind is an abstract class that give the Actor the ability to "understand/respond" to one or more messages.  In this case, we create a new external "Mind" for the Actor that allows it to understand and respond to both messages "Hello" and "World".
```java
public class HelloWorldMind extends SimpleExternalActorMind {
    
}
```

The new class HelloWorldMind must override the method process() and define which messages to handle.  
```java
@Override
public void process() throws Exception {
}
```

Now, we just take the message into a local variable.  This is an optional step.
```java
String msg = (String) context().envelope().message();
```

This step is very important.  Here, we are telling the Actor what/how to do with messages.

We are giving to the Actor a new Mind! with "knowledge" about how interact with others actors.
```java
if (msg.equals("Hello")) {
    context().myself().tell("World", context().myself());
}
if (msg.equals("World")) {
    context().myself().tell("Hello", context().myself());
}
```

At this moment, we can use the HelloWorldMind to create a new Actor into the ActorSystem and send messages to it.

```java
ISystem system = new ActorSystem("DEMO");  //Create a System where Actors can live.
IActorRef hello = system.createActor(new HelloWorldMind(), "HELLOWORLD"); //Create an intelligent Actor with the HelloWorldMind.
```

To send a message to Actor hello, we just call its "tell" method:
```java
hello.tell("Hello", null);
```

#### More examples

[Here](https://github.com/yarhoslavme/YMActors/tree/master/src/main/java/me/yarhoslav/ymactors/examples)

### Using YMActors framework in your code

#### Import as Maven library

```xml
<!-- https://mvnrepository.com/artifact/me.yarhoslav/YMActors -->
<dependency>
    <groupId>me.yarhoslav</groupId>
    <artifactId>YMActors</artifactId>
    <version>0.2.2</version>
</dependency>
```

#### Import as Jar file

[YMActors-0.2.2.jar](http://central.maven.org/maven2/me/yarhoslav/YMActors/0.2.2/YMActors-0.2.2.jar)

#### Github releases

Check [Github releases](https://github.com/yarhoslavme/YMActors/releases)

#### More import options

Check [this](https://mvnrepository.com/artifact/me.yarhoslav/YMActors/0.2.2) general repository

#### Wiki

Comming soon...

#### Blog

[Yarhoslav's blog](http://yarhoslav.me)


In heavy development... New features every day!.  Stay tuned. :+1:
