package com.gregswebserver.catan.client;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType {

    Net_Connect, //Connects to a remote server and opens a socket.
    Net_Disconnect, //Disconnects from a remote server.
    Net_Join, //Sends Identity information to the server.
    Net_Leave, //Removes identity from the server, and disconnects from any active games.
}
