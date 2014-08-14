package com.gregswebserver.catan.server;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType {

    Net_Start,
    Net_Stop,
    Client_Connect,
    Client_Disconnect,
    Client_Join,
    Client_Leave,
    Group_Create,
    Group_Delete,
    Group_Join,
    Group_Leave,

}
