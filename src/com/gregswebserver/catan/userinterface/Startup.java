package com.gregswebserver.catan.userinterface;

import com.gregswebserver.catan.debug.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Greg on 8/10/2014.
 * Splash screen to allow either starting a server or logging into one.
 */
public class Startup extends JFrame {
    private JPanel contentPane;
    private JTextField[] fields = new JTextField[TextField.values().length];
    private JButton[] buttons = new JButton[ActionButton.values().length];

    public Startup() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        setResizable(false);
        setTitle("Settlers of Catan - Startup");

        Dimension windowSize = new Dimension(300, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowSize.width, windowSize.height);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

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
                    startClient(Integer.parseInt(fields[3].getText()), fields[2].getText(), fields[0].getText(), fields[1].getText());
                } catch (Exception ex) {
                    Debug.printStack(ex);
                }
            }
        });
        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startServer(Integer.parseInt(fields[3].getText()), fields[1].getText());
                } catch (Exception ex) {
                    Debug.printStack(ex);
                }
            }
        });
    }

    private void startClient(int port, String hostname, String username, String password) {
        dispose();
    }

    private void startServer(int port, String password) {
        dispose();
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
