package catan.client.input;

import catan.client.Client;
import catan.common.event.QueuedInputThread;

import java.awt.Point;
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

    public InputListener(Client client, Clickable root) {
        this.client = client;
        listener = new UserEventDelegator();
        queue = new HoverTimingThread();
        queue.start();
        this.root = root;
        nullClickable = new ClickableAdapter() {
            @Override
            public String toString() {
                return "NullClickable";
            }
        };
        selected = nullClickable;
        hover = nullClickable;
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
        }
    }

    private void select(MouseEvent e) {
        update(e);
        if (selected != hover) {
            selected.onDeselect(listener);
            selected = hover;
            selected.onSelect(listener);
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        selected.onKeyTyped(listener, keyEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        selected.onKeyPressed(listener, keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        selected.onKeyReleased(listener, keyEvent);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        select(mouseEvent);
        selected.onMouseClick(listener, mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        select(mouseEvent);
        selected.onMousePress(listener, mouseEvent);
        dragStart = mouseEvent.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        selected.onMouseRelease(listener, mouseEvent);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        update(mouseEvent);
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        update(mouseEvent);
        Point dragEnd = mouseEvent.getPoint();
        dragEnd.translate(-dragStart.x, -dragStart.y);
        dragStart = mouseEvent.getPoint();
        selected.onMouseDrag(listener, dragEnd);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        update(mouseEvent);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        update(mouseWheelEvent);
        selected.onMouseScroll(listener, mouseWheelEvent);
    }

    public String toString() {
        return "InputListener";
    }

    private class HoverEvent implements Delayed {

        private final Clickable clickable;
        private final long targetTime;

        private HoverEvent(Clickable clickable, Number msecDelay) {
            this.clickable = clickable;
            targetTime = System.currentTimeMillis() + msecDelay.longValue();
        }

        @Override
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(targetTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed t) {
            return Long.compare(targetTime, ((HoverEvent) t).targetTime);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HoverEvent other = (HoverEvent) o;

            if (targetTime != other.targetTime) return false;
            return clickable != null ? clickable.equals(other.clickable) : other.clickable == null;
        }

        @Override
        public int hashCode() {
            int result = clickable != null ? clickable.hashCode() : 0;
            result = 31 * result + (int) (targetTime ^ (targetTime >>> 32));
            return result;
        }
    }

    private class HoverTimingThread extends QueuedInputThread<HoverEvent> {

        private HoverTimingThread() {
            super(new DelayQueue<>());
        }

        @Override
        protected void execute() throws ThreadStopException {
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
            if ((event != null) && (event.getType() == UserEventType.Linger_Trigger)) {
                HoverEvent hoverEvent = new HoverEvent(hover, (Number) event.getPayload());
                queue.addEvent(hoverEvent);
            } else {
                if (event != null)
                    client.addEvent(event);
            }
        }
    }
}
