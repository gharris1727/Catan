package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Greg on 8/12/2014.
 * Dispatcher to take in keyboard/mouse events and dispatch them to the client as GenericEvents.
 * Generates GameEvents, ChatEvents, and ClientEvents, based on color hit maps.
 * Added as a listener to the ClientWindow.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final Logger logger;
    private final Client client;
    private final Clickable nullClickable;
    private final Clickable root;
    private Clickable selected;
    private Point dragStart;

    public InputListener(Client client, Clickable root) {
        this.logger = client.logger;
        this.client = client;
        this.root = root;
        this.nullClickable = new Clickable() {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return log("Mouse clicked on null");
            }

            @Override
            public UserEvent onKeyTyped(KeyEvent event) {
                return log("Key typed on null");
            }

            @Override
            public UserEvent onMouseScroll(int rot) {
                return log("Mouse scrolled on null");
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return log("Mouse dragged on null");
            }

            @Override
            public UserEvent onSelect() {
                return log("Selected null");
            }

            @Override
            public UserEvent onDeselect() {
                return log("Deselected null");
            }

            @Override
            public Clickable getClickable(Point p) {
                log("Getting clickable from null");
                return this;
            }

            private UserEvent log(String message) {
//                logger.log(message + " nothing.", LogLevel.DEBUG);
                return null;
            }

            public String toString() {
                return "nullClickable";
            }
        };
        this.selected = nullClickable;
    }

    private void sendEvent(UserEvent event) {
        if (event != null)
            client.addEvent(event);
    }

    private void updateClickable(MouseEvent e) {
        Clickable found = root.getClickable(e.getPoint());
        Clickable next = (found == null) ? nullClickable : found;
        if (selected != next) {
            logger.log("Clickable changed to " + next, LogLevel.DEBUG);
            sendEvent(selected.onDeselect());
            selected = next;
            sendEvent(selected.onSelect());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        sendEvent(selected.onKeyTyped(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        updateClickable(e);
        sendEvent(selected.onMouseClick(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        updateClickable(e);
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point dragEnd = e.getPoint();
        dragEnd.translate(-dragStart.x, -dragStart.y);
        dragStart = e.getPoint();
        sendEvent(selected.onMouseDrag(dragEnd));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        sendEvent(selected.onMouseScroll(e.getWheelRotation()));
    }

    public String toString() {
        return "InputListener";
    }
}
