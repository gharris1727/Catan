package com.gregswebserver.catan.common.event;

/**
 * Created by greg on 6/11/16.
 * An event that contains a typed payload
 */
public interface TypedPayloadEvent<O, T extends EventType> {

    O getOrigin();

    T getType();

    Object getPayload();

}
