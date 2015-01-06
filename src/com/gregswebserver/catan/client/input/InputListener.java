package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
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
    private Clickable hitbox;
    private Clickable selected;
    private Point dragStart;

    public InputListener(Client client) {
        logger = client.logger;
        this.client = client;
        this.nullClickable = new Clickable() {
            public UserEvent onMouseClick(MouseEvent event) {
                return log("Mouse clicked");
            }

            public UserEvent onKeyTyped(KeyEvent event) {
                return log("Key typed");
            }

            public UserEvent onMouseScroll(int wheelRotation) {
                return log("Mouse scrolled");
            }

            public UserEvent onMouseDrag(Point p) {
                return log("Mouse dragged");
            }

            public UserEvent onSelect() {
                return log("Selected");
            }

            public UserEvent onDeselect() {
                return log("Deselected");
            }

            public Clickable getClickable(Point p) {
                log("Getting clickable from");
                return null;
            }

            private UserEvent log(String message) {
//                logger.log(message + " nothing.", LogLevel.DEBUG);
                return null;
            }
        };
        selected = nullClickable;
        hitbox = nullClickable;
    }

    public UserEvent onMouseClick(MouseEvent event) {
        return selected.onMouseClick(event);
    }

    public UserEvent onKeyTyped(KeyEvent event) {
        return selected.onKeyTyped(event);
    }

    public UserEvent onMouseScroll(int wheelRotation) {
        return selected.onMouseScroll(wheelRotation);
    }

    public UserEvent onMouseDrag(Point p) {
        return selected.onMouseDrag(p);
    }

    public UserEvent onSelect() {
        return selected.onSelect();
    }

    public UserEvent onDeselect() {
        return selected.onDeselect();
    }

    public Clickable getClickable(Point p) {
        Clickable fromHitbox = hitbox.getClickable(p);
        return (fromHitbox == null) ? nullClickable : fromHitbox;
    }

    private void sendEvent(UserEvent event) {
        if (event != null)
            client.addEvent(event);
    }

    public void setHitbox(Clickable hitbox) {
        this.hitbox = (hitbox == null) ? nullClickable : hitbox;
    }

    private void updateClickable(MouseEvent e) {
        if (hitbox != null) {
            Clickable next = getClickable(e.getPoint());
            if (selected != next) {
                sendEvent(onDeselect());
                selected = next;
                sendEvent(onSelect());
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        sendEvent(onKeyTyped(e));
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        updateClickable(e);
        sendEvent(onMouseClick(e));
    }

    public void mousePressed(MouseEvent e) {
        updateClickable(e);
        dragStart = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Point dragEnd = e.getPoint();
        dragEnd.translate(-dragStart.x, -dragStart.y);
        dragStart = e.getPoint();
        sendEvent(onMouseDrag(dragEnd));
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        sendEvent(onMouseScroll(e.getWheelRotation()));
    }

    public String toString() {
        return "InputListener";
    }
}
