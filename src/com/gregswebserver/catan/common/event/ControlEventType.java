package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.Password;
import com.gregswebserver.catan.common.structure.MatchmakingPool;

/**
 * Created by Greg on 10/16/2014.
 * Type determining the purpose of a lobby event that controls repeating data between clients.
 */
public enum ControlEventType implements EventType {

    Pass_Change(Password.class), //Client -> Server, when the client requests a password change.
    Pass_Change_Success(null), //Server -> Client, when a password change succeeds.
    Pass_Change_Failure(null), //Server -> Client, when a password change fails.
    Server_Disconnect(String.class), //Server -> Broadcast, When the server disconnects, String why.
    Client_Disconnect(String.class), //Client -> Server, when a client disconnects, String why.
    User_Pool_Sync(MatchmakingPool.class); //Server -> Client, when first joining to send the current client pool state.


    private final Class payloadType;

    ControlEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
