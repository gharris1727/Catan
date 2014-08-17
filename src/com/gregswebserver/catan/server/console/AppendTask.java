package com.gregswebserver.catan.server.console;

import javax.swing.*;

/**
 * Created by Greg on 8/12/2014.
 * Task that appends it's text to the console at the correct time.
 */
public class AppendTask implements Runnable {

    private JTextArea textArea;
    private String text;

    public AppendTask(JTextArea textArea, String text) {
        this.textArea = textArea;
        this.text = text;
    }

    public void run() {
        textArea.append(text + "\n");
    }
}
