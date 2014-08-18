package com.gregswebserver.catan.client.chat;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderEventType;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;

/**
 * Created by Greg on 8/13/2014.
 * Thread to process a queue of ChatEvents that are coming from the Client.
 * Stores chat data in a ChatLog object, and can give that data to the RenderThread.
 */
public class ChatThread extends QueuedInputThread {

    private Client client;
    private ChatLog chatLog;

    public ChatThread(Client client) {
        super(client.logger);
        this.client = client;
        chatLog = new ChatLog();
    }

    //Process the ChatEvent queue and add messages to the ChatLog.
    public void execute() throws ThreadStop {
        ChatEvent event = (ChatEvent) getEvent(true);
        chatLog.addMessage(event.getMessage());
        client.addEvent(new RenderEvent(this, RenderEventType.Chat_Update, chatLog));
    }
}
