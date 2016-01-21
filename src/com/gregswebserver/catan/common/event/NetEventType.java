package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.crypto.UserLogin;

/**
 * Created by Greg on 1/18/2015.
 * The type of NetEvent that is being created.
 */
public enum NetEventType implements EventType {

    Log_In(UserLogin.class),
    Log_In_Success(AuthToken.class),
    Log_In_Failure(String.class),
    Disconnect(String.class),
    Link_Error(String.class),
    External_Event(ExternalEvent.class);

    private final Class payloadType;

    NetEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
