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
        this.nullClickable = new NullClickable();
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
            sendEvent(hover.onUnHover());
            hover = next;
            sendEvent(hover.onHover());
            //client.logger.log("Hovered "+ hover, LogLevel.DEBUG);
        }
    }

    private void select(MouseEvent e) {
        update(e);
        if (selected != hover) {
            sendEvent(selected.onDeselect());
            selected = hover;
            sendEvent(selected.onSelect());
            //client.logger.log("Selected "+ selected, LogLevel.DEBUG);
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
    public void mouseExited(MouseEvent e) { }

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
        sendEvent(selected.onMouseScroll(e));
    }

    public String toString() {
        return "InputListener";
    }

}
