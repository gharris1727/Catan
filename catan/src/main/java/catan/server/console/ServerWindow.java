package catan.server.console;

import catan.common.CoreWindow;
import catan.server.Server;
import catan.server.ServerEvent;
import catan.server.ServerEventType;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Greg on 8/11/2014.
 * GraphicalConsole window for CLI access to the server application
 */
public class ServerWindow extends CoreWindow {

    private final Server server;
    private final Logger logger;
    private final GraphicalConsole graphicalConsole;

    public ServerWindow(Server server, Logger logger, Console console) {
        super("Settlers of Catan - Server", new Dimension(800, 600), true);
        this.server = server;
        this.logger = logger;
        setLayout(new BorderLayout());
        graphicalConsole = new GraphicalConsole(console);
        getContentPane().add(graphicalConsole);
        logger.addHandler(graphicalConsole.handler);
        display();
        setVisible(true);
    }

    @Override
    protected void onClose() {
        logger.removeHandler(graphicalConsole.handler);
        server.addEvent(new ServerEvent(server, ServerEventType.Quit_All, null));
    }

    @Override
    protected void onResize(Dimension size) {
    }

    public String toString() {
        return "ServerWindow";
    }

    private class GraphicalConsole extends JPanel implements KeyListener {

        private final Console console;
        private final JTextArea textArea;
        private final StringBuilder commandLine;
        private final Handler handler;

        private GraphicalConsole(Console console) {
            setLayout(new BorderLayout());
            this.console = console;
            textArea = new JTextArea(20, 30);
            commandLine = new StringBuilder();
            textArea.addKeyListener(this);
            add(new JScrollPane(textArea));
            handler = new LogHandler();
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
            char typed = keyEvent.getKeyChar();
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
        public void keyPressed(KeyEvent keyEvent) {
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
        }

        private void append(String text) {
            SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
        }

        private class LogHandler extends Handler {
            @Override
            public void publish(LogRecord logRecord) {
                //TODO: overwrite the user input and then print the half command out below the log message.
                append(logRecord.getMessage());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        }
    }
}
