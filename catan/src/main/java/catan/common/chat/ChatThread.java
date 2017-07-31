package catan.common.chat;

import catan.common.event.QueuedInputThread;

/**
 * Created by Greg on 8/13/2014.
 * Thread to process a queue of ChatEvents that are coming from the Client.
 * Stores chat data in a ChatLog object, and can give that data to the RenderThread.
 */
public class ChatThread extends QueuedInputThread<ChatEvent> {

    private final ChatLog chatLog;

    public ChatThread() {
        chatLog = new ChatLog();
    }

    //Process the ChatEvent queue and add messages to the ChatLog.
    @Override
    public void execute() throws ThreadStopException {
        ChatEvent event = getEvent(true);
        chatLog.addMessage(event.getMessage());
    }

    public String toString() {
        return "ChatThread";
    }
}
