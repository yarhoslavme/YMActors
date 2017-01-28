package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author YarhoslavME
 */
public class BroadcastActor extends DefaultActorHandler {

    public static enum MSG {
        SUBSCRIBE, UNSUBSCRIBE;
    };

    static final Logger LOGGER = getLogger(BroadcastActor.class.getName());

    private final Map<String, IActorRef> susbcribers = new HashMap<>();

    private void susbcribe(IActorRef pActor) {
        susbcribers.put(pActor.getName(), pActor);
    }

    private void unsusbcribe(IActorRef pActor) {
        susbcribers.remove(pActor.getName());
    }

    private void publish(Object pMsg) {
        if (susbcribers.isEmpty()) {
            return;
        }

        susbcribers.entrySet().forEach((entry) -> {
            entry.getValue().tell(pMsg, this.getMyself().getSender());
        });
    }

    @Override
    public void process(Object msg) {
        if (!(msg instanceof BroadcastActor.MSG)) {
            publish(msg);
        }
        switch ((BroadcastActor.MSG) msg) {
            case SUBSCRIBE:
                susbcribe(this.getMyself().getSender());
                break;
            case UNSUBSCRIBE:
                unsusbcribe(this.getMyself().getSender());
                break;
        }
    }
}
