package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderEventType;
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

    private Client client;
    private Logger logger;
    private ScreenArea hitbox;
    private Point dragStart;

    public InputListener(Client client) {
        logger = client.logger;
        this.client = client;
    }

    public void setHitbox(ScreenArea hitbox) {
        this.hitbox = hitbox;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, new Point(0, -5)));
                break;
            case KeyEvent.VK_DOWN:
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, new Point(0, 5)));
                break;
            case KeyEvent.VK_LEFT:
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, new Point(-5, 0)));
                break;
            case KeyEvent.VK_RIGHT:
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, new Point(5, 0)));
                break;
            //TODO: Try and control spam somehow
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Clickable c = hitbox.getClickable(e.getPoint());
        if (c != null) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    c.onLeftClick();
                    break;
                case MouseEvent.BUTTON2:
                    c.onMiddleClick();
                    break;
                case MouseEvent.BUTTON3:
                    c.onRightClick();
                    break;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
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
        dragEnd = new Point(dragEnd.x / 2, dragEnd.y / 2);
        dragStart = e.getPoint();
        client.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, dragEnd));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    public String toString() {
        return "InputListener";
    }
}
