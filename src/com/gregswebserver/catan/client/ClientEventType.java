package com.gregswebserver.catan.client;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType {

    Net_Connect, //Connects to a remote server and opens a socket.
    Net_Disconnect, //Disconnects from a remote server.
    Canvas_Update, //Event sent from the renderThread which updates the Canvas object kept in the ClientWindow.
    Hitbox_Update, //Event sent from the renderThread that updates the hitbox information in ClientListener.
}
