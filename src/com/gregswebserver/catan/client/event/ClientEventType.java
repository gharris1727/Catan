package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.common.crypto.ServerLogin;
import com.gregswebserver.catan.common.event.EventType;

import java.awt.*;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with a ClientEvent to send further information as to what the client needs to do.
 * Stores general client actions.
 */
public enum ClientEventType implements EventType {

    Quit_All(null), //Kills all client processes and exits the game.
    Net_Connect(ServerLogin.class), //Begin connecting to a remote server given by netID.
    Net_Connected(null), //Connection established.
    Net_Disconnect(null), //Disconnects from a remote server.
    Net_Disconnected(null), // Connection is finished.
    Canvas_Update(Canvas.class), //Event sent from the renderThread which updates the Canvas object kept in the ClientWindow.
    Hitbox_Update(ScreenArea.class); //Event sent from the renderThread that updates the hitbox information in ClientListener.

    private Class payloadType;

    ClientEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }
}