package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.renderer.ScreenObject;
import com.gregswebserver.catan.client.input.clickables.Clickable;
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
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    //TODO: a lot of stuff in here.

    private final Logger logger;
    private final Client client;
    private ScreenObject hitbox;
    private Point dragStart;
    private Clickable lastSelected;

    public InputListener(Client client) {
        logger = client.logger;
        this.client = client;
    }

    public void setHitbox(ScreenObject hitbox) {
        this.hitbox = hitbox;
    }

    private void updateClickable(MouseEvent e) {
        if (hitbox != null)
            lastSelected = hitbox.getClickable(e.getPoint());
        logger.log("lastSelected: " + lastSelected, LogLevel.DEBUG);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (lastSelected != null)
            lastSelected.onKeyTyped(e.getKeyCode());
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
        if (lastSelected != null)
            lastSelected.onMouseClick(e.getButton());
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
        if (lastSelected != null)
            lastSelected.onMouseDrag(dragEnd);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (lastSelected != null)
            lastSelected.onMouseScroll(e.getWheelRotation());
    }

    public String toString() {
        return "InputListener";
    }
}
