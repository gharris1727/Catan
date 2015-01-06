package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by Greg on 1/4/2015.
 * Event sent from the input event to the main client
 */
public class UserEvent extends InternalEvent<UserEventType> {

    public UserEvent(Object origin, UserEventType type, Object payload) {
        super(origin, type, payload);
    }
}
