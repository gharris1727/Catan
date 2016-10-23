package catan.common.chat;

import catan.common.event.QueuedInputThread;
import catan.common.log.Logger;

/**
 * Created by Greg on 8/13/2014.
 * Thread to process a queue of ChatEvents that are coming from the Client.
 * Stores chat data in a ChatLog object, and can give that data to the RenderThread.
 */
public class ChatThread extends QueuedInputThread<ChatEvent> {

    private final ChatLog chatLog;

    public ChatThread(Logger logger) {
        super(logger);
        chatLog = new ChatLog();
    }

    //Process the ChatEvent queue and add messages to the ChatLog.
    @Override
    public void execute() throws ThreadStop {
        ChatEvent event = getEvent(true);
        chatLog.addMessage(event.getMessage());
    }

    public String toString() {
        return "ChatThread";
    }
}