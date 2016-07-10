package catan.common.chat;

import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greg on 8/12/2014.
 * Created when someone sends a chat message anywhere on the client.
 * Sent to the Client, which relays to the server and a local ChatLog.
 * Terminates in a ChatLog hosted on all of the receiving clients.
 */
public class ChatEvent extends ExternalEvent<ChatEventType> {

    private final String message;

    //General purpose constructor.
    public ChatEvent(Username origin, String message) {
        super(origin, ChatEventType.Lobby, null);
        this.message = format(message);
    }

    //Custom constructor.
    public ChatEvent(Username origin, ChatEventType type, Username destination, String message) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatEvent)) return false;
        if (!super.equals(o)) return false;

        ChatEvent chatEvent = (ChatEvent) o;

        return message != null ? message.equals(chatEvent.message) : chatEvent.message == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
