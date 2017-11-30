package me.yarhoslav.ymactors.core.system;

import me.yarhoslav.ymactors.core.actors.IActorRef;
import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public class SchedulerTask implements Runnable {

        private final IActorRef pReceiver;
        private final IEnvelope pEnvelope;

        public SchedulerTask(IActorRef pReceiver, IEnvelope pEnvelope) {
            this.pReceiver = pReceiver;
            this.pEnvelope = pEnvelope;
        }

        @Override
        public void run() {
            pReceiver.tell(pEnvelope);
        }
    }
