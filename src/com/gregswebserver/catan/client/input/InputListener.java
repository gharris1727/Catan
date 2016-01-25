package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Greg on 8/12/2014.
 * Dispatcher to take in keyboard/mouse events and dispatch them to the client as GenericEvents.
 * Generates GameEvents, ChatEvents, and ClientEvents, based on color hit maps.
 * Added as a listener to the ClientWindow.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final Client client;
    private final Clickable nullClickable;
    private final Clickable root;
    private Clickable selected;
    private Clickable hover;
    private Point dragStart;

    public InputListener(Client client, Clickable root) {
        this.client = client;
        this.root = root;
        this.nullClickable = new Clickable() {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return null;
            }

            @Override
            public UserEvent onMousePress(MouseEvent e) {
                return null;
            }

            @Override
            public UserEvent onMouseRelease(MouseEvent e) {
                return null;
            }

            @Override
            public UserEvent onKeyTyped(KeyEvent event) {
                return null;
            }

            @Override
            public UserEvent onKeyPressed(KeyEvent e) {
                return null;
            }

            @Override
            public UserEvent onKeyReleased(KeyEvent e) {
                return null;
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return null;
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return null;
            }

            @Override
            public UserEvent onHover() {
                return null;
            }

            @Override
            public UserEvent onUnHover() {
                return null;
            }

            @Override
            public UserEvent onSelect() {
                return null;
            }

            @Override
            public UserEvent onDeselect() {
                return null;
            }

            @Override
            public Clickable getClickable(Point p) {
                return this;
            }

            public String toString() {
                return "nullClickable";
            }
        };
        this.selected = nullClickable;
        this.hover = nullClickable;
    }

    private void sendEvent(UserEvent event, UserEvent fallback) {
        if (event != null)
            client.addEvent(event);
        else if (fallback != null)
            client.addEvent(fallback);
    }

    private void update(MouseEvent e) {
        Clickable found = root.getClickable(e.getPoint());
        Clickable next = (found == null) ? nullClickable : found;
        if (hover != next) {
            sendEvent(hover.onUnHover(), null);
            hover = next;
            sendEvent(hover.onHover(), null);
//            logger.log("Hovered "+ hover, LogLevel.DEBUG);
        }
    }

    private void select(MouseEvent e) {
        update(e);
        if (selected != hover) {
            sendEvent(selected.onDeselect(), null);
            selected = hover;
            sendEvent(selected.onSelect(), null);
//            logger.log("Selected "+ selected, LogLevel.DEBUG);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        sendEvent(selected.onKeyTyped(e), hover.onKeyTyped(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        sendEvent(selected.onKeyPressed(e), hover.onKeyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        sendEvent(selected.onKeyReleased(e), hover.onKeyReleased(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        select(e);
        sendEvent(selected.onMouseClick(e), null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        select(e);
        sendEvent(selected.onMousePress(e), null);
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sendEvent(selected.onMouseRelease(e), null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        update(e);
    }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        update(e);
        Point dragEnd = e.getPoint();
        dragEnd.translate(-dragStart.x, -dragStart.y);
        dragStart = e.getPoint();
        sendEvent(selected.onMouseDrag(dragEnd), null);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        update(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        sendEvent(selected.onMouseScroll(e), hover.onMouseScroll(e));
    }

    public String toString() {
        return "InputListener";
    }
}
