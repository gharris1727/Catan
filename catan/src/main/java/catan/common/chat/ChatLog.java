package catan.common.chat;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Greg on 8/13/2014.
 * Created by the ChatThread, as a place to store messages from ChatEvents.
 * Accepts a message and appends it to the chat history.
 * Limited size, any messages beyond maxLines are deleted.
 */
public class ChatLog implements Iterable<String> {
    private int maxLines = 50;
    private int maxWidth = 50;
    private final LinkedList<String> lines;

    public ChatLog() {
        lines = new LinkedList<>();
    }

    //Change the maximum scroll back after the ChatLog has been created.
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    //Parses a message, and limits each line to maxWidth characters.
    public void addMessage(String message) {
        String[] words = message.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if ((line.length() + word.length() + 1) < maxWidth) {
                line.append(" ");
                line.append(word);
            } else {
                append(line.toString());
                line = new StringBuilder("    " + word);
            }
        }
        append(line.toString());
    }

    //Appends a line to the end of the file, and checks it's length.
    private void append(String line) {
        if (lines.size() >= maxLines) {
            lines.removeLast();
        }
        lines.add(0, line);
    }

    @Override
    public Iterator<String> iterator() {
        return lines.iterator();
    }
}
