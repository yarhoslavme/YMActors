package me.yarhoslav.ymactors.core.actors;

import me.yarhoslav.ymactors.core.messages.IEnvelope;

/**
 *
 * @author yarhoslavme
 */
public interface IWorker {

    public void newMessage(IEnvelope pEnvelope);

    public void stop();

    public void taskDone();

    public void execute();
}
