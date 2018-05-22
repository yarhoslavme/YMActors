# YMActors

[![GitHub version](https://badge.fury.io/gh/yarhoslavme%2FYMActors.svg)](https://badge.fury.io/gh/yarhoslavme%2FYMActors)
[![Travis CI](https://travis-ci.org/yarhoslavme/YMActors.svg?branch=master)](https://travis-ci.org/yarhoslavme/YMActors)
[![Join the chat at https://gitter.im/YMActors/Lobby](https://badges.gitter.im/YMActors/Lobby.svg)](https://gitter.im/YMActors/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


## Actors framework for Java

**Goals:**

>1. Create a pure/lightweight Java actor framework, easy to use.
>2. Inspired by AKKA (See [Akka.io](http://akka.io)) but written from scratch.

The YMActors project gives you all tools you need to write highly resilient/elastic/multithreaded systems, easy and without all the headaches of multithreading programming.

## Example

Simple "Hello World" example. 

First, create one actor HelloWorld that can accept two messages ("Hello" and "World") and will respond accordingly.

```java
public class HelloWorld extends SimpleExternalActorMind {
    private final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Override# YMActors

[![GitHub version](https://badge.fury.io/gh/yarhoslavme%2FYMActors.svg)](https://badge.fury.io/gh/yarhoslavme%2FYMActors)
[![Travis CI](https://travis-ci.org/yarhoslavme/YMActors.svg?branch=master)](https://travis-ci.org/yarhoslavme/YMActors)
[![Join the chat at https://gitter.im/YMActors/Lobby](https://badges.gitter.im/YMActors/Lobby.svg)](https://gitter.im/YMActors/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


Actors framework for Java

Goals:

1. Create pure Java Actors framework, easy to use.
2. Inspired by AKKA (See AKKA.IO)

In heavy development... New features every day!.  Stay tuned.

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

```java
public class HelloWorld extends SimpleExternalActorMind
```

//TODO:
Hablar del ejemplo.
Hablar del Ping-Pong.
Describir el modelo implementado.  (System, Actor, ActorMinds, Minions).
Publicar en yarhoslav.me toda esta informacion y hacer un link entre ambos sitios.
Hacer publicidad (buscar los sitios de wikipedia y poner la libreria).
Usar: https://www.brianstorti.com/the-actor-model/
https://doc.akka.io/docs/akka/2.5.5/java/guide/actors-intro.html
Rective Manifesto




In heavy development... New features every day!.  Stay tuned.
