package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 * @author yarhoslavme
 */
public interface IWorker extends Runnable {

    void newMessage(IEnvelope pEnvelope);

    void stop();
}
