package com.gregswebserver.catan.client.chat;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent along inside of a ChatEvent to indicate it's purpose.
 * Functions as a routing aid, and allows the server to rebroadcast it the right direction.
 */
public enum ChatEventType {

    Broadcast,  //Message should be rebroadcast to all clients connected to the server.
    Lobby, //Message should be rebroadcast to only the clients in a local Group.
    Private; //Message should be forwarded to only the destination client.
}
