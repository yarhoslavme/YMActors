package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manjaro
 */
public class HelloWorld2 extends SimpleExternalActorMind {

    long counter = 0;
    long start = System.currentTimeMillis();
    long stop = 0;
    private final Logger logger = LoggerFactory.getLogger(HelloWorld2.class);

    @Override
    public void process() throws Exception {
        if (!(context().envelope().message() instanceof String)) {
            return;
        }
        counter++;

        String msg = (String) context().envelope().message();

        if (counter >= 5000000) {
            context().myself().tell(PoisonPill.INSTANCE, context().myself());
            stop = System.currentTimeMillis();
            logger.info("Mensaje POISONPILL, contador {} en {} milis", counter, stop - start);
        }
        if (msg.equals("Hello")) {
            context().myself().tell("World", context().myself());
        }
        if (msg.equals("World")) {
            context().myself().tell("Hello", context().myself());
        }
    }
}
