package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/12/2014.
 * Event for transporting chat messages across the network.
 */
public class ChatEvent extends ExternalEvent {

    private String message;

    public ChatEvent(Identity origin) {
        super(origin);
    }

    public String getMessage() {
        return message;
    }
}
