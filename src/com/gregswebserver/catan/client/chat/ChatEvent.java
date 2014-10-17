package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.network.Identity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greg on 8/12/2014.
 * Created when someone sends a chat message anywhere on the client.
 * Sent to the Client, which relays to the server and a local ChatLog.
 * Terminates in a ChatLog hosted on all of the receiving clients.
 */
public class ChatEvent extends ExternalEvent<ChatEventType> {

    public final String message;

    //General purpose constructor.
    public ChatEvent(Identity origin, String message) {
        super(origin, ChatEventType.Lobby, null);
        this.message = format(message);
    }

    //Custom constructor.
    public ChatEvent(Identity origin, ChatEventType type, Identity destination, String message) {
        super(origin, type, destination);
        this.message = format(message);
    }

    private String format(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String username = getOrigin().username;
        return "<" + timestamp + "> \"" + username + "\" " + message;
    }

    public String getMessage() {
        return message;
    }
}
