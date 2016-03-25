package com.gregswebserver.catan.common.event;

import java.io.Serializable;

/**
 * Created by Greg on 12/29/2014.
 * Superclass of custom object payloads sent in ExternalEvents
 * Other payloads are used, but all implement serializable.
 */
public abstract class EventPayload implements Serializable {

    public static final long serialVersionUID = 1L;
}
