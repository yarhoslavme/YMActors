package com.yarhoslav.ymactors.core.actors;

/**
 *
 * @author yarhoslavme
 */
public abstract class SimpleActorMind implements IActorMind {
    
    public final IActorContext owner;
    
    public SimpleActorMind(IActorContext pOwner) {
        owner = pOwner;
    }
    
    public IActorContext context() {
        return owner;
    }
    //TODO: Create a context for the user.
    //TODO: Create a public method initialize.  Can be called only ones
    
}
