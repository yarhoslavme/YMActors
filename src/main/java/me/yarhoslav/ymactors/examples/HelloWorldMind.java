package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manjaro
 */
public class HelloWorldMind extends SimpleExternalActorMind {
    private final Logger logger = LoggerFactory.getLogger(HelloWorldMind.class);

    @Override
    public void process() {
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
