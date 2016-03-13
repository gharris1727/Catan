package com.gregswebserver.catan.server.console;

import com.gregswebserver.catan.common.log.LogEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.LogListener;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.server.Server;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Greg on 8/12/2014.
 * Console that accepts user input, prints the log output, and can execute user input.
 */
public class Console extends JPanel implements UserInput, LogListener {

    private final Logger logger;
    private final JTextArea textArea;
    private int userInputStart = 0;
    private Server server;

    public Console(Logger logger) {
        this.logger = logger;
        setLayout(new BorderLayout());
        textArea = new JTextArea(20, 30);
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new ProtectedDocumentFilter(this));
        add(new JScrollPane(textArea));

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //TODO: process user input and control the server.
            }
        });
    }

    @Override
    public String getUserInput() {
        try {
            return textArea.getText(userInputStart, textArea.getCaretPosition() - userInputStart).trim();
        } catch (BadLocationException e) {
            logger.log("Console error", e, LogLevel.WARN);
            return "";
        }
    }

    @Override
    public int getUserInputStart() {
        return userInputStart;
    }

    @Override
    public void onLogEvent(LogEvent e) {
        append(e.toString());
    }

    private void append(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

}
