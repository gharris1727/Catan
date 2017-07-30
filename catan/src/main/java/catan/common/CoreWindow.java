package catan.common;

import catan.common.log.LogLevel;
import catan.common.log.Logger;

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
    private final Logger logger;
    private final Object closeNotification;

    protected CoreWindow(String title, Dimension size, boolean resizable, Logger logger) {
        this.size = size;
        this.logger = logger;
        closeNotification = new Object();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(e, LogLevel.WARN);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                synchronized (closeNotification) {
                    setVisible(false);
                    onClose();
                    closeNotification.notify();
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                //Uses the insets of the window to get the real content size of the window.
                Dimension newSize = componentEvent.getComponent().getSize();
                Insets i = getInsets();
                newSize.width -= i.left + i.right;
                newSize.height -= i.bottom + i.top;
                onResize(newSize);
            }
        });
        setTitle(title);
        setResizable(resizable);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    protected void display() {
        setMinimumSize(size);
        setLocationRelativeTo(null);
    }

    // Gracefully close any pertinent threads when the window is closed.
    protected abstract void onClose();

    protected abstract void onResize(Dimension size);

    public void waitForClose() {
        try {
            synchronized (closeNotification) {
                while (isVisible()) {
                    closeNotification.wait();
                }
            }
        } catch (InterruptedException e) {
            logger.log(e, LogLevel.WARN);
        }
    }
}
