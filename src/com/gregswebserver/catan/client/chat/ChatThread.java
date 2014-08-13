package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.event.EventQueueThread;
import com.gregswebserver.catan.log.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greg on 8/13/2014.
 * Chat system to keep a local record of chat messages received by the client.
 */
public class ChatThread extends EventQueueThread<ChatEvent> {

    private ChatLog chatLog;

    public ChatThread(Logger logger) {
        super(logger);
        chatLog = new ChatLog();
    }

    public void execute() {
        ChatEvent event = getEvent(true);
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String username = event.getOrigin().username;
        String message = event.getMessage();
        chatLog.addMessage("<" + timestamp + "> \"" + username + "\" " + message);
    }

    public ChatLog getChatLog() {
        return chatLog;
    }
}
