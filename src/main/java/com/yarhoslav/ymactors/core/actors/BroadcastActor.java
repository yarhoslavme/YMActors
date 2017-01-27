package com.yarhoslav.ymactors.core.actors;

import com.yarhoslav.ymactors.core.ActorsContainer;
import com.yarhoslav.ymactors.core.DefaultActorHandler;
import com.yarhoslav.ymactors.core.interfaces.IActorRef;
import com.yarhoslav.ymactors.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author YarhoslavME
 */
public class BroadcastActor extends DefaultActorHandler {

    static final Logger logger = getLogger(BroadcastActor.class.getName());

    private final Map<String, String> suscribers = new HashMap<>();

    private void suscribe(String pActor) {
        suscribers.put(pActor, pActor);
    }

    private void unsuscribe(String pActor) {
        suscribers.remove(pActor);
    }

    private void publish(Object pMsg) {
        if (suscribers.isEmpty()) {
            return;
        }
        suscribers.keySet().stream().forEach((_nombre) -> {
            ActorsContainer _acc = this.getMyself().getContainer();
            IActorRef _destino = _acc.findActor(_nombre);
            if (_destino != null) {
                _destino.tell(pMsg, this.getMyself().getSender());
            } else {
                unsuscribe(_nombre);
            }
        });
    }

    @Override
    public void process(Object msg) {
        if (!(msg instanceof String)) return;
        switch ((String) msg) {
            case Constants.MSG_SUSCRIBE:
                suscribe(this.getMyself().getSender().getName());
                break;
            case Constants.MSG_UNSUSCRIBE:
                unsuscribe(this.getMyself().getSender().getName());
                break;
            default:
                publish(msg);
        }
    }
}
