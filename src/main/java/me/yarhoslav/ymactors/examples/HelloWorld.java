package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manjaro
 */
public class HelloWorld extends SimpleExternalActorMind {
    private final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Override
    public void process() throws Exception {
        String msg = (String) context().envelope().message();
        logger.info(msg);

        if (msg.equals("Hello")) {
            context().myself().tell("World", context().myself());
        }
        if (msg.equals("World")) {
            context().myself().tell("Hello", context().myself());
        }
    }
}
