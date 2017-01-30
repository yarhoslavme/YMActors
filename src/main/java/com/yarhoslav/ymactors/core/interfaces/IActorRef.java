package com.yarhoslav.ymactors.core.interfaces;

import com.yarhoslav.ymactors.core.messages.BroadCastMsg;
import java.util.Map;

/**
 *
 * @author YarhoslavME
 */
public interface IActorRef extends Runnable {

    public String getName();

    public IActorContext getContext();

    public IActorRef getSender();

    public boolean isAlive();

    public boolean isIdle();
    
    public IActorRef start() throws Exception;

    public void tell(Object pData);

    public void tell(Object pData, IActorRef pSender);

    public void tell(BroadCastMsg pMsg);
}
