package com.gregswebserver.catan.server;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType {

    Client_Connect,
    Client_Disconnect,
    Client_Join,
    Client_Leave,
    Lobby_Create,
    Lobby_Delete,
    Lobby_Join,
    Lobby_Leave,

}
