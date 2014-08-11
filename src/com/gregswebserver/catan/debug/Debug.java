package com.gregswebserver.catan.debug;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Greg on 8/10/2014.
 * Debug class to catch errors on execution and handle them gracefully.
 */
public class Debug extends JFrame {

    private static Debug debugWindow;
    private JPanel contentPane;

    public Debug() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setResizable(false);
        setTitle("Debug");

        Dimension windowSize = new Dimension(512, 384);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowSize.width, windowSize.height);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setVisible(true);
    }

    public static void printStack(Exception e) {
        setupWindow();
    }

    public static void setupWindow() {
        if (debugWindow == null) {
            debugWindow = new Debug();
        }
    }
}
