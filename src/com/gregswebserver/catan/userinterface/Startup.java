package com.gregswebserver.catan.userinterface;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.network.NetID;
import com.gregswebserver.catan.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

/**
 * Created by Greg on 8/10/2014.
 * Splash screen to allow either starting a server or log into one.
 */
public class Startup extends GenericWindow {

    private JPanel contentPane;
    private JTextField[] fields = new JTextField[TextField.values().length];
    private JButton[] buttons = new JButton[ActionButton.values().length];

    public Startup(Logger logger) {
        super("Settlers of Catan - Startup", new Dimension(300, 500), false, logger);
        contentPane = new JPanel();
        setContentPane(contentPane);
        setLayout(null);

        int i = 0;
        for (TextField tf : TextField.values()) {
            JTextField field = new JTextField();
            JLabel label = new JLabel(tf.label);
            JLabel example = new JLabel("Ex: " + tf.example);
            field.setBounds(tf.getBounds());
            label.setSize(tf.size);
            example.setSize(tf.size);
            label.setLocation(tf.position.x, tf.position.y - 20);
            example.setLocation(tf.position.x, tf.position.y + 20);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            example.setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(field);
            contentPane.add(label);
            contentPane.add(example);
            fields[i] = field;
            i++;
        }

        int j = 0;
        for (ActionButton ab : ActionButton.values()) {
            JButton button = new JButton(ab.label);
            button.setBounds(ab.getBounds());
            button.setHorizontalAlignment(JButton.CENTER);
            contentPane.add(button);
            buttons[j] = button;
            j++;
        }

        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startClient(getHost(), getPort(), getUsername(), getPassword());
                } catch (Exception ex) {
                    logger.log("Error starting client", ex, LogLevel.ERROR);
                }
            }
        });
        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startServer(getPort(), getPassword());
                } catch (Exception ex) {
                    logger.log("Error while starting server", ex, LogLevel.ERROR);
                }
            }
        });
        setVisible(true);
    }

    private int getPort() {
        try {
            return 25000;//TODO: REMOVE ME!
//            return Integer.parseInt(fields[3].getText());
        } catch (Exception e) {
//            logger.log("Invalid port entered", e, LogLevel.POPUP);
        }
        return 0;
    }

    private InetAddress getHost() {
        try {
            return InetAddress.getByName("localhost"); //TODO: REMOVE ME!
//            return InetAddress.getByName(fields[2].getText());
        } catch (Exception e) {
//            logger.log("Hostname Field Error", e, LogLevel.POPUP);
        }
        return null;
    }

    private String getPassword() {
        return fields[1].getText();
    }

    private String getUsername() {
        return fields[0].getText();
    }

    private void startClient(InetAddress host, int port, String username, String password) {
        Client client = new Client(new NetID(host, port));
        dispose();
    }

    private void startServer(int port, String password) {
        Server server = new Server();
        server.start(port);
    }

    protected void onClose() {
    }

    protected void onResize(Dimension size) {
        //Nothing can happen cause this can't be resized.
    }

    private enum TextField {

        Username("Username", "bob", 150, 64, 150, 24),
        Password("Password", "hunter2", 150, 128, 150, 24),
        Hostname("Hostname", "google.com", 150, 192, 150, 24),
        Port("Port", "25332", 150, 256, 150, 24);

        private String label;
        private String example;
        private Point position;
        private Dimension size;


        TextField(String label, String example, int x, int y, int w, int h) {
            this.label = label;
            this.example = example;
            this.position = new Point(x - w / 2, y - h / 2);
            this.size = new Dimension(w, h);
        }

        public Rectangle getBounds() {
            return new Rectangle(position, size);
        }
    }

    private enum ActionButton {

        Client("Start Client", 100, 350, 80, 24),
        Server("Start Server", 200, 350, 80, 24);

        private String label;
        private Point position;
        private Dimension size;

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
