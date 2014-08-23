package com.gregswebserver.catan.server;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType {

    Client_Connect, //Fired when a client connects with Identity information.
    Client_Disconnect, //Fired when a client disconnects.
    Lobby_Create, //Fired when a lobby is created.
    Lobby_Delete, //Fired when a lobby is deleted.
    Lobby_Join, //Fired when a client joins a lobby.
    Lobby_Leave, //Fired when a client leaves a lobby, or is removed.
    Lobby_Update, //Fired when a lobby is created or removed as to update the lobby list client side.

}
