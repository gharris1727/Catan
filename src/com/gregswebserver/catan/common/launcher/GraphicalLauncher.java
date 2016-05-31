package com.gregswebserver.catan.common.launcher;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.CoreWindow;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.server.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Greg on 8/10/2014.
 * Launch window to select further actions. Replaces the command-line interface.
 */
public class GraphicalLauncher extends CoreWindow {

    private final Logger logger;

    public GraphicalLauncher(StartupOptions options, Logger logger) {
        //TODO: Clean up the graphical launcher to include features from the command line.
        super("Settlers of Catan - Launcher", new Dimension(300, 500), false, logger);
        this.logger = logger;
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        setLayout(null);

        int j = 0;
        JButton[] buttons = new JButton[ActionButton.values().length];
        for (ActionButton ab : ActionButton.values()) {
            JButton button = new JButton(ab.label);
            button.setBounds(ab.getBounds());
            button.setHorizontalAlignment(JButton.CENTER);
            contentPane.add(button);
            buttons[j] = button;
            j++;
        }

        buttons[0].addActionListener(e -> {
            try {
                startClient();
            } catch (Exception ex) {
                logger.log("Error starting client", ex, LogLevel.ERROR);
            }
        });
        buttons[1].addActionListener(e -> {
            try {
                startServer();
            } catch (Exception ex) {
                logger.log("Error while starting server", ex, LogLevel.ERROR);
            }
        });
        display();
        setVisible(true);
    }


    private void startClient() {
        new Client(logger);
    }

    private void startServer() {
        new Server(logger, 25000);
    }

    @Override
    protected void onClose() {
        dispose();
    }

    @Override
    protected void onResize(Dimension size) { }

    public String toString() {
        return "GraphicalLauncher";
    }


    private enum ActionButton {

        Client("Start Client", 80, 350, 120, 24),
        Server("Start Server", 220, 350, 120, 24);

        private final String label;
        private final Point position;
        private final Dimension size;

        ActionButton(String label, int x, int y, int w, int h) {
            this.label = label;
            this.position = new Point(x - w / 2, y - h / 2);
            this.size = new Dimension(w, h);
        }

        private Rectangle getBounds() {
            return new Rectangle(position, size);
        }
    }

}
