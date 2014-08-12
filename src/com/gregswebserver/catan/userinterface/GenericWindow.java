package com.gregswebserver.catan.userinterface;

import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Generic window that extends JFrame and has some basic config settings.
 */
public abstract class GenericWindow extends JFrame {

    protected Logger logger;

    public GenericWindow(String title, Dimension d, boolean resizable, Logger logger) {
        this.logger = logger;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(e, LogLevel.WARN);
        }
        setTitle(title);
        setSize(d);
        setLocationRelativeTo(null);
        setResizable(resizable);
        if (resizable) setMinimumSize(d);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
