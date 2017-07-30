package catan.client.ui.game;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.client.graphics.ui.*;
import catan.client.input.UserEvent;
import catan.client.input.UserEventListener;
import catan.client.input.UserEventType;
import catan.client.structure.GameManager;
import catan.client.ui.PopupWindow;
import catan.common.game.event.GameHistory;
import catan.common.game.teams.TeamColor;
import catan.common.locale.game.LocalizedGameHistoryPrinter;
import catan.common.resources.GraphicSet;
import catan.common.util.MutableInteger;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 3/11/16.
 * A game timeline allowing the user to view the progression of the game, and manually step through the history of the game.
 */
public class TimelineRegion extends ConfigurableScreenRegion implements Updatable {

    //Required instance details
    private final GameManager manager;

    //Optional interactions
    private ContextRegion context;
    private TimelinePopup popup;

    //Configuration dependencies
    private Map<TeamColor, GraphicSet> eventGraphics;

    //Sub-regions
    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;

    public TimelineRegion(GameManager manager) {
        super("Timeline", 1, "timeline");
        //Store instance information
        this.manager = manager;
        //Create sub-regions
        background = new TiledBackground();
        timeline = new ScrollingScreenContainer("TimelineScrollContainer", 1, new EventList());
        //Add everything to the screen.
        add(background).setClickable(this);
        add(timeline);
        timeline.enableTransparency();
    }

    public void setContext(ContextRegion context) {
        this.context = context;
    }

    @Override
    public void loadConfig(UIConfig config) {
        eventGraphics = new EnumMap<>(TeamColor.class);
        TeamColorSwaps teamColorSwaps = new TeamColorSwaps(config.getTeamColors());
        for (TeamColor teamColor : TeamColor.values())
            eventGraphics.put(teamColor,new GraphicSet(config.getLayout(), "source", teamColorSwaps.getSwaps(teamColor)));
    }

    @Override
    public void update() {
        timeline.update();
    }

    private class EventList extends ScrollingScreenRegion {
        private int lastCount;
        private int eventSpacing;
        private int eventBuffer;
        private int eventHeight;
        private int delay;

        private EventList() {
            super("TimelineEventList", 0, "list");
            enableTransparency();
        }

        @Override
        public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
            scroll(-8 * event.getUnitsToScroll(), 0);
        }

        @Override
        public void onMouseDrag(UserEventListener listener, Point p) {
            scroll(p.x, p.y);
        }

        @Override
        public void loadConfig(UIConfig config) {
            eventSpacing = config.getLayout().getInt("spacing");
            eventBuffer = config.getLayout().getInt("buffer");
            eventHeight = config.getLayout().getInt("height");
            delay = config.getLayout().getInt("delay");
            setInsets(new Insets(0, eventBuffer, 0, eventBuffer));
        }

        @Override
        protected void renderContents() {
            clear();
            MutableInteger width = new MutableInteger();
            MutableInteger count = new MutableInteger();
            manager.getRemoteGame().readHistory(history -> {
                    add(new EventListElement(count.get(), history)).setPosition(new Point(width.get(), 0));
                    width.inc(eventSpacing);
                }
            );
            width.inc(eventBuffer);
            setMask(new RectangularMask(new Dimension(width.get(), eventHeight)));
            //Scroll the bar over as new events come in.
            scroll(-eventSpacing * (count.get() - lastCount), 0);
            lastCount = count.get();
        }

        @Override
        public void update() {
            forceRender();
        }

        private class EventListElement extends GraphicObject {

            private final int index;
            private final GameHistory event;

            private EventListElement(int index, GameHistory event) {
                super("Event " + index, 0);
                this.index = index;
                this.event = event;
                setGraphic(eventGraphics.get(event.getTeam()).getGraphic(event.getGameEvent().getType()));
            }
            @Override
            public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
                EventList.this.onMouseScroll(listener, event);
            }
            @Override
            public void onMouseDrag(UserEventListener listener, Point p) {
                EventList.this.onMouseDrag(listener, p);
            }
            @Override
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                if (context != null)
                    context.targetHistory(index, this.event);
            }
            @Override
            public void onHover(UserEventListener listener) {
                listener.onUserEvent(new UserEvent(this, UserEventType.Linger_Trigger, delay));
            }

            @Override
            public void onUnHover(UserEventListener listener) {
                synchronized(EventList.this) {
                    if (popup != null) {
                        popup.expire();
                        popup = null;
                    }
                }
            }

            @Override
            public void onLinger(UserEventListener listener) {
                synchronized (EventList.this) {
                    if (popup != null) {
                        popup.expire();
                    }
                    popup = new TimelinePopup(event);
                    popup.display();
                }
            }
        }
    }

    private class TimelinePopup extends PopupWindow {

        //Instance information
        private final GameHistory event;

        //Configuration dependencies
        private LocalizedGameHistoryPrinter printer;
        private int spacing;

        //Sub-Regions
        private final TiledBackground background;
        private final TextLabel label;

        protected TimelinePopup(GameHistory event) {
            super("TimelinePopup", "history", TimelineRegion.this);
            this.event = event;
            background = new EdgedTiledBackground();
            label = new TextLabel("HistoryPopupLabel", 1, "label", "");
            add(background).setClickable(this);
            add(label).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            super.resizeContents(mask);
            background.setMask(mask);
        }

        @Override
        public void loadConfig(UIConfig config) {
            printer = new LocalizedGameHistoryPrinter(config.getLocale());
            spacing = config.getLayout().getInt("spacing");
        }

        @Override
        protected void renderContents() {
            label.setText(printer.getLocalization(event));
            RenderMask labelSize = label.getGraphic().getMask();
            setMask(new RectangularMask(new Dimension(labelSize.getWidth() + (spacing << 1), labelSize.getHeight() + (spacing << 1))));
            center(label);
        }

        @Override
        public void update() {
            forceRender();
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        timeline.setMask(mask);
        background.setMask(mask);
    }
}
