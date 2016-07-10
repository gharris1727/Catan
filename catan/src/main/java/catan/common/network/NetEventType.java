package catan.common.network;

import catan.common.crypto.AuthToken;
import catan.common.event.EventType;
import catan.common.event.ExternalEvent;
import catan.common.structure.UserLogin;

/**
 * Created by Greg on 1/18/2015.
 * The type of NetEvent that is being created.
 */
public enum NetEventType implements EventType {

    Register(UserLogin.class),
    Register_Success(null),
    Register_Failure(String.class),
    Log_In(UserLogin.class),
    Auth_Success(AuthToken.class),
    Auth_Failure(String.class),
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
