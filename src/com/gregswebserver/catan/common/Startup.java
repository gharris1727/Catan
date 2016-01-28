package com.gregswebserver.catan.common;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.server.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Greg on 8/10/2014.
 * Splash screen to allow either starting a server or log into one.
 */
public class Startup extends CoreWindow {

    private final JPanel contentPane;
    private final JButton[] buttons = new JButton[ActionButton.values().length];

    public Startup(final Logger logger) {
        //TODO: rewrite this whole thing. this whole thing is an abomination.
        super("Settlers of Catan - Startup", new Dimension(300, 500), false, logger);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setLayout(null);

        int j = 0;
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
                logger.log("Link_Error starting client", ex, LogLevel.ERROR);
            }
        });
        buttons[1].addActionListener(e -> {
            try {
                startServer();
            } catch (Exception ex) {
                logger.log("Link_Error while starting server", ex, LogLevel.ERROR);
            }
        });
        display();
        setVisible(true);
    }


    private void startClient() {
        new Client();
    }

    private void startServer() {
        new Server(25000);
    }

    @Override
    protected void onClose() {
        dispose();
    }

    public String toString() {
        return "Startup";
    }


    private enum ActionButton {

        Client("Start Client", 100, 350, 80, 24),
        Server("Start Server", 200, 350, 80, 24);

        private final String label;
        private final Point position;
        private final Dimension size;

        ActionButton(String label, int x, int y, int w, int h) {
            this.label = label;
            this.position = new Point(x - w / 2, y - h / 2);
            this.size = new Dimension(w, h);
        }

        public Rectangle getBounds() {
            return new Rectangle(position, size);
        }
    }

}
