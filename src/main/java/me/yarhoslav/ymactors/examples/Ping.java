package me.yarhoslav.ymactors.examples;

import me.yarhoslav.ymactors.core.messages.PoisonPill;
import me.yarhoslav.ymactors.core.minds.SimpleExternalActorMind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manjaro
 */
public class Ping extends SimpleExternalActorMind {

    long counter = 0;
    long start = System.currentTimeMillis();
    long stop = 0;
    private final Logger logger = LoggerFactory.getLogger(Ping.class);

    @Override
    public void process() throws Exception {
        if (!(context().envelope().message() instanceof String)) {
            return;
        }
        counter++;
        String msg = (String) context().envelope().message();
        logger.info(msg);

        if (counter >= 40000*5) {
            context().myself().tell(PoisonPill.INSTANCE, context().myself());
            stop = System.currentTimeMillis();
            logger.info("Mensaje POISONPILL, contador {} en {} milis", counter, stop - start);
        }
        if (msg.equals("ping")) {
            context().envelope().sender().tell("pong", context().myself());
        }
    }
}
