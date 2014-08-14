package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.log.Logger;

/**
 * Created by Greg on 8/13/2014.
 * Thread to process a queue of ChatEvents that are coming from the Client.
 * Stores chat data in a ChatLog object, and can give that data to the RenderThread.
 */
public class ChatThread extends QueuedInputThread<ChatEvent> {

    private ChatLog chatLog;

    public ChatThread(Logger logger) {
        super(logger);
        chatLog = new ChatLog();
    }

    //Process the ChatEvent queue and add messages to the ChatLog.
    public void execute() {
        ChatEvent event = getEvent(true);
        chatLog.addMessage(event.getMessage());
    }

    //TODO: add method of retrieving the ChatLog data.
}
