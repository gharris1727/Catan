package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.crypto.UserLogin;

/**
 * Created by Greg on 1/18/2015.
 * The type of NetEvent that is being created.
 */
public enum NetEventType implements EventType {

    Authenticate(UserLogin.class),
    AuthenticationSuccess(AuthToken.class),
    AuthenticationFailure(String.class),
    Disconnect(String.class),
    Error(String.class),
    External(ExternalEvent.class); //TODO: break down into smaller classes.

    private Class payloadType;

    NetEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }
}
