package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
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
    private Clickable hover;
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
            public UserEvent onMousePress(MouseEvent e) {
                return log("Mouse pressed null");
            }

            @Override
            public UserEvent onMouseRelease(MouseEvent e) {
                return log("Mouse released null");
            }

            @Override
            public UserEvent onKeyTyped(KeyEvent event) {
                return log("Key typed on null");
            }

            @Override
            public UserEvent onKeyPressed(KeyEvent e) {
                return log("Key pressed null");
            }

            @Override
            public UserEvent onKeyReleased(KeyEvent e) {
                return log("Key released null");
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
            public UserEvent onHover() {
                return log("Hovered null");
            }

            @Override
            public UserEvent onUnHover() {
                return log("DeHovered null");
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
        this.hover = nullClickable;
    }

    private void sendEvent(UserEvent event) {
        if (event != null)
            client.addEvent(event);
    }

    private void update(MouseEvent e) {
        Clickable found = root.getClickable(e.getPoint());
        Clickable next = (found == null) ? nullClickable : found;
        if (hover != next) {
            sendEvent(selected.onUnHover());
            hover = next;
            sendEvent(selected.onHover());
        }
    }

    private void select(MouseEvent e) {
        update(e);
        if (selected != hover) {
            sendEvent(selected.onDeselect());
            selected = hover;
            sendEvent(selected.onSelect());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        sendEvent(selected.onKeyTyped(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        sendEvent(selected.onKeyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        sendEvent(selected.onKeyReleased(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        select(e);
        sendEvent(selected.onMouseClick(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        select(e);
        sendEvent(selected.onMousePress(e));
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sendEvent(selected.onMouseRelease(e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        update(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        update(e);
        Point dragEnd = e.getPoint();
        dragEnd.translate(-dragStart.x, -dragStart.y);
        dragStart = e.getPoint();
        sendEvent(selected.onMouseDrag(dragEnd));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        update(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        UserEvent selectedAction = selected.onMouseScroll(e.getWheelRotation());
        sendEvent((selectedAction == null) ? hover.onMouseScroll(e.getWheelRotation()) : selectedAction);
    }

    public String toString() {
        return "InputListener";
    }
}
