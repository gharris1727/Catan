package com.gregswebserver.catan.event;

import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin identity information.
 */
public abstract class ExternalEvent extends GenericEvent {

    public final Identity origin;

    protected ExternalEvent(Identity origin) {
        this.origin = origin;
    }

    public String toString() {
        return "Origin: " + origin;
    }
}
