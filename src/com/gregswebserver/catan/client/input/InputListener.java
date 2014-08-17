package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.log.Logger;

import java.awt.event.*;

/**
 * Created by Greg on 8/12/2014.
 * Dispatcher to take in keyboard/mouse events and dispatch them to the client as GenericEvents.
 * Generates GameEvents, ChatEvents, and ClientEvents, based on hitboxes.
 * Added as a listener to the ClientWindow.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener {

    private Hitbox hitbox;
    private Logger logger;

    public InputListener(Client client) {
        logger = client.logger;
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
