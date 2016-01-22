package com.gregswebserver.catan.common;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

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
public abstract class CoreWindow extends JFrame {

    private final Dimension size;

    protected CoreWindow(String title, Dimension size, boolean resizable, Logger logger) {
        this.size = size;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(e, LogLevel.WARN);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                onMove();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                //Uses the insets of the window to get the real content size of the window.
                Dimension size = e.getComponent().getSize();
                Insets i = getInsets();
                size.width -= i.left + i.right;
                size.height -= i.bottom + i.top;
                onResize(size);
            }
        });
        setTitle(title);
        setResizable(resizable);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    protected void display() {
        setSize(size);
        setMinimumSize(size);
        setLocationRelativeTo(null);
    }

    // Gracefully close any pertinent threads when the window is closed.
    protected void onClose() {
    }

    protected void onMove() {
    }

    protected void onResize(Dimension size) {
    }
}
