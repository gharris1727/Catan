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
 * Generates GameEvents, ChatEvents, and ClientEvents, based on hitboxes.
 * Added as a listener to the ClientWindow.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, Clickable {

    private final Logger logger;
    private final Client client;
    private final Clickable nullClickable;
    private Clickable rootClickable;
    private Clickable selected;
    private Point dragStart;

    public InputListener(Client client) {
        logger = client.logger;
        this.client = client;
        this.nullClickable = new Clickable() {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return log("Mouse clicked");
            }

            @Override
            public UserEvent onKeyTyped(KeyEvent event) {
                return log("Key typed");
            }

            @Override
            public UserEvent onMouseScroll(int rot) {
                return log("Mouse scrolled");
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return log("Mouse dragged");
            }

            @Override
            public UserEvent onSelect() {
                return log("Selected");
            }

            @Override
            public UserEvent onDeselect() {
                return log("Deselected");
            }

            @Override
            public Clickable getClickable(Point p) {
                log("Getting clickable from");
                return null;
            }

            private UserEvent log(String message) {
//                logger.log(message + " nothing.", LogLevel.DEBUG);
                return null;
            }

            public String toString() {
                return "nullClickable";
            }
        };
        selected = nullClickable;
        rootClickable = nullClickable;
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return selected.onMouseClick(event);
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        return selected.onKeyTyped(event);
    }

    @Override
    public UserEvent onMouseScroll(int rot) {
        return selected.onMouseScroll(rot);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return selected.onMouseDrag(p);
    }

    @Override
    public UserEvent onSelect() {
        return selected.onSelect();
    }

    @Override
    public UserEvent onDeselect() {
        return selected.onDeselect();
    }

    @Override
    public Clickable getClickable(Point p) {
        Clickable found = rootClickable.getClickable(p);
        return (found == null) ? nullClickable : found;
    }

    private void sendEvent(UserEvent event) {
        if (event != null)
            client.addEvent(event);
    }

    public void setClickable(Clickable clickable) {
        this.rootClickable = (clickable == null) ? nullClickable : clickable;
    }

    private void updateClickable(MouseEvent e) {
        if (rootClickable != null) {
            Clickable next = getClickable(e.getPoint());
            if (selected != next) {
                logger.log("Clickable changed to " + next, LogLevel.DEBUG);
                sendEvent(onDeselect());
                selected = next;
                sendEvent(onSelect());
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        sendEvent(onKeyTyped(e));
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
        sendEvent(onMouseClick(e));
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
        sendEvent(onMouseDrag(dragEnd));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        sendEvent(onMouseScroll(e.getWheelRotation()));
    }

    public String toString() {
        return "InputListener";
    }
}
