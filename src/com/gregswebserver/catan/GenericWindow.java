package com.gregswebserver.catan;

import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Greg on 8/11/2014.
 * Generic window that extends JFrame and has some basic config settings.
 */
public abstract class GenericWindow extends JFrame {

    protected GenericWindow(String title, Dimension d, boolean resizable, Logger logger) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(e, LogLevel.WARN);
        }
        setTitle(title);
        setSize(d);
        setMinimumSize(d);
        setLocationRelativeTo(null);
        setResizable(resizable);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
                super.windowClosing(e);
            }
        });
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                onResize(e.getComponent().getSize());
                super.componentResized(e);
            }
        });
    }

    // Gracefully close any pertinent threads when the window is closed.
    protected abstract void onClose();

    protected abstract void onResize(Dimension size);
}
