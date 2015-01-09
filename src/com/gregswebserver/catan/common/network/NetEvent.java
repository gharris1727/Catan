package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Network event for communicating between client and server.
 */
public class NetEvent extends GenericEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    public final int sessionID;
    public final ExternalEvent event;

    public NetEvent(int sessionID, ExternalEvent event) {
        this.sessionID = sessionID;
        this.event = event;
    }
}
