package catan.server.console;

import catan.common.CoreWindow;
import catan.common.log.LogEvent;
import catan.common.log.LogListener;
import catan.server.Server;
import catan.server.ServerEvent;
import catan.server.ServerEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Greg on 8/11/2014.
 * GraphicalConsole window for CLI access to the server application
 */
public class ServerWindow extends CoreWindow {

    private final Server server;
    private final GraphicalConsole graphicalConsole;

    public ServerWindow(Server server, Console console) {
        super("Settlers of Catan - Server", new Dimension(800, 600), true, server.logger);
        this.server = server;
        setLayout(new BorderLayout());
        graphicalConsole = new GraphicalConsole(console);
        getContentPane().add(graphicalConsole);
        server.logger.addListener(graphicalConsole);
        display();
        setVisible(true);
    }

    @Override
    protected void onClose() {
        server.logger.removeListener(graphicalConsole);
        server.addEvent(new ServerEvent(server, ServerEventType.Quit_All, null));
    }

    @Override
    protected void onResize(Dimension size) {
    }

    public String toString() {
        return "ServerWindow";
    }

    private class GraphicalConsole extends JPanel implements LogListener, KeyListener {

        private final Console console;
        private final JTextArea textArea;
        private final StringBuilder commandLine;

        private GraphicalConsole(Console console) {
            setLayout(new BorderLayout());
            this.console = console;
            textArea = new JTextArea(20, 30);
            commandLine = new StringBuilder();
            textArea.addKeyListener(this);
            add(new JScrollPane(textArea));
        }

        @Override
        public void keyTyped(KeyEvent event) {
            char typed = event.getKeyChar();
            if (typed == '\n') {
                console.process(commandLine.toString());
                commandLine.setLength(0);
            } else if (typed == '\b') {
                int len = commandLine.length() - 1;
                if (len < 0)
                    len = 0;
                commandLine.setLength(len);
            } else {
                commandLine.append(typed);
            }
            //TODO: preview what the user is typing and make the text selectable.
        }

        @Override
        public void keyPressed(KeyEvent event) {
        }

        @Override
        public void keyReleased(KeyEvent event) {
        }

        @Override
        public void onLogEvent(LogEvent e) {
            //TODO: overwrite the user input and then print the half command out below the log message.
            append(e.toString());
        }

        private void append(String text) {
            SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
        }

    }
}
