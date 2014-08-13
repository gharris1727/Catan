package com.gregswebserver.catan.client.chat;

import java.util.ArrayList;

/**
 * Created by Greg on 8/13/2014.
 * Storage system for chat storage, accepts
 */
public class ChatLog {

    private int maxLines = 50;
    private ArrayList<String> lines;

    public ChatLog() {
        lines = new ArrayList<>(50);
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        lines.ensureCapacity(maxLines);
    }

    public String getLastLine() {
        return lines.get(0);
    }

    public void addMessage(String message) {
        lines.add(0, message);
    }
}
