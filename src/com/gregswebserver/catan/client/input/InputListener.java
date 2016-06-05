package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.log.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by Greg on 8/12/2014.
 * Dispatcher to take in keyboard/mouse events and dispatch them to the client as GenericEvents.
 * Generates GameEvents, ChatEvents, and ClientEvents, based on color hit maps.
 * Added as a listener to the ClientWindow.
 */
public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final Client client;
    private final HoverTimingThread queue;
    private final Clickable nullClickable;
    private final Clickable root;
    private Clickable selected;
    private Clickable hover;
    private Point dragStart;

    public InputListener(Client client, Clickable root) {
        this.client = client;
        this.queue = new HoverTimingThread(client.logger);
        queue.start();
        this.root = root;
        this.nullClickable = new ClickableAdapter() {
            @Override
            public String toString() {
                return "NullClickable";
            }
        };
        this.selected = nullClickable;
        this.hover = nullClickable;
    }

    public QueuedInputThread<HoverEvent> getThread() {
        return queue;
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
            UserEvent event = hover.onHover();
            if (event != null && event.getType() == UserEventType.Linger_Trigger) {
                HoverEvent hoverEvent = new HoverEvent(hover, (Long) event.getPayload());
                client.logger.debug(this, "Waiting " + hoverEvent.getDelay(TimeUnit.MILLISECONDS) + "msecs");
                queue.addEvent(hoverEvent);
             } else {
                sendEvent(event);
            }
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

    private class HoverEvent implements Delayed {

        private final Clickable clickable;
        private final long targetTime;

        private HoverEvent(Clickable clickable, long msecDelay) {
            this.clickable = clickable;
            this.targetTime = System.currentTimeMillis() + msecDelay;
        }

        @Override
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(targetTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed delayed) {
            long d = this.targetTime - ((HoverEvent) delayed).targetTime;
            return ((d == 0) ? 0 : ((d < 0) ? -1 : 1));
        }
    }

    private class HoverTimingThread extends QueuedInputThread<HoverEvent> {

        private HoverTimingThread(Logger logger) {
            super(logger, new DelayQueue<>());
        }

        @Override
        protected void execute() throws ThreadStop {
            HoverEvent event = getEvent(true);
            if (event.clickable == hover)
                sendEvent(event.clickable.onLinger());
            logger.debug(this,"Lingered on " + event.clickable);
        }

        @Override
        public String toString() {
            return "HoverTimingThread";
        }
    }
}
