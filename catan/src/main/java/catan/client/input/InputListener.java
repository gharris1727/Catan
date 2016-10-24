package catan.client.input;

import catan.client.Client;
import catan.common.event.QueuedInputThread;
import catan.common.log.Logger;

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
    private final UserEventListener listener;
    private final HoverTimingThread queue;
    private final Clickable nullClickable;
    private final Clickable root;
    private Clickable selected;
    private Clickable hover;
    private Point dragStart;

    public InputListener(Client client, Logger logger, Clickable root) {
        this.client = client;
        listener = new UserEventDelegator();
        this.queue = new HoverTimingThread(logger);
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

    private void update(MouseEvent e) {
        Clickable found = root.getClickable(e.getPoint());
        Clickable next = (found == null) ? nullClickable : found;
        if (hover != next) {
            hover.onUnHover(listener);
            hover = next;
            hover.onHover(listener);
            //client.logger.log("Hovered "+ hover, LogLevel.DEBUG);
        }
    }

    private void select(MouseEvent e) {
        update(e);
        if (selected != hover) {
            selected.onDeselect(listener);
            selected = hover;
            selected.onSelect(listener);
            //client.logger.log("Selected "+ selected, LogLevel.DEBUG);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        selected.onKeyTyped(listener, e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        selected.onKeyPressed(listener, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        selected.onKeyReleased(listener, e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        select(e);
        selected.onMouseClick(listener, e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        select(e);
        selected.onMousePress(listener, e);
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selected.onMouseRelease(listener, e);
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
        selected.onMouseDrag(listener, dragEnd);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        update(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        update(e);
        selected.onMouseScroll(listener, e);
    }

    public String toString() {
        return "InputListener";
    }

    private class HoverEvent implements Delayed {

        private final Clickable clickable;
        private final long targetTime;

        private HoverEvent(Clickable clickable, Number msecDelay) {
            this.clickable = clickable;
            this.targetTime = System.currentTimeMillis() + msecDelay.longValue();
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
                event.clickable.onLinger(listener);
            //logger.debug(this,"Lingered on " + event.clickable);
        }

        @Override
        public String toString() {
            return "HoverTimingThread";
        }
    }

    private class UserEventDelegator implements UserEventListener {

        @Override
        public void onUserEvent(UserEvent event) {
            if (event != null && event.getType() == UserEventType.Linger_Trigger) {
                HoverEvent hoverEvent = new HoverEvent(hover, (Number) event.getPayload());
                queue.addEvent(hoverEvent);
            } else {
                if (event != null)
                    client.addEvent(event);
            }
        }
    }
}
