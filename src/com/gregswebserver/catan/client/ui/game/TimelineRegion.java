package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.ui.PopupWindow;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameHistory;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.locale.game.LocalizedGameHistoryPrinter;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by greg on 3/11/16.
 * A game timeline allowing the user to view the progression of the game, and manually step through the history of the game.
 */
public class TimelineRegion extends ConfigurableScreenRegion implements Updatable {

    //Required instance details
    private final List<GameHistory> history;
    private final PlayerPool players;

    //Optional interactions
    private ContextRegion context;
    private HistoryPopup popup;

    //Configuration dependencies
    private Map<TeamColor, GraphicSet> eventGraphics;

    //Sub-regions
    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;

    public TimelineRegion(CatanGame game) {
        super("Timeline", 1, "timeline");
        //Store instance information
        history = game.getHistory();
        players = game.getPlayers();
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
            lastCount = history.size();
            enableTransparency();
        }

        @Override
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            scroll(-8 * event.getUnitsToScroll(), 0);
            return null;
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            scroll(p.x, p.y);
            return null;
        }

        @Override
        public void loadConfig(UIConfig config) {
            eventSpacing = config.getLayout().getInt("spacing");
            eventBuffer = config.getLayout().getInt("buffer");
            eventHeight = config.getLayout().getInt("height");
            delay = config.getLayout().getInt("info.delay");
            setInsets(new Insets(0, eventBuffer, 0, eventBuffer));
        }

        @Override
        protected void renderContents() {
            clear();
            int width = 0;
            for (int i = 0; i < history.size(); i++) {
                add(new EventListElement(i)).setPosition(new Point(width, 0));
                width += eventSpacing;
            }
            width += eventBuffer;
            setMask(new RectangularMask(new Dimension(width, eventHeight)));
            //Scroll the bar over as new events come in.
            scroll(-eventSpacing * (history.size() - lastCount), 0);
            lastCount = history.size();
        }

        @Override
        public void update() {
            forceRender();
        }

        private class EventListElement extends GraphicObject {

            private final int index;

            private EventListElement(int index) {
                super("Event " + index, 0);
                this.index = index;
                GameEvent event = history.get(index).getGameEvent();
                TeamColor teamColor = event.getOrigin() == null ? TeamColor.None : players.getPlayer(event.getOrigin()).getTeamColor();
                setGraphic(eventGraphics.get(teamColor).getGraphic(event.getType()));
            }
            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return EventList.this.onMouseScroll(event);
            }
            @Override
            public UserEvent onMouseDrag(Point p) {
                return EventList.this.onMouseDrag(p);
            }
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                if (context != null)
                    context.targetHistory(index);
                return null;
            }
            @Override
            public UserEvent onHover() {
                return new UserEvent(this, UserEventType.Linger_Trigger, delay);
            }

            @Override
            public UserEvent onUnHover() {
                synchronized(EventList.this) {
                    if (popup != null) {
                        UserEvent out = popup.expire();
                        popup = null;
                        return out;
                    } else {
                        return null;
                    }
                }
            }

            @Override
            public UserEvent onLinger() {
                synchronized (EventList.this) {
                    HistoryPopup old = popup;
                    popup = new HistoryPopup(history.get(index));
                    popup.setConfig(getConfig());
                    if (old != null)
                        return new UserEvent(this, UserEventType.Composite_Event, Arrays.asList(old.expire(), popup.display()));
                    else
                        return popup.display();
                }
            }
        }
    }

    private class HistoryPopup extends PopupWindow {

        //Instance information
        private final GameHistory event;

        //Configuration dependencies
        LocalizedGameHistoryPrinter printer;

        //Sub-Regions
        private final TiledBackground background;
        private final TextLabel label;

        protected HistoryPopup(GameHistory event) {
            super("HistoryPopup", "info", TimelineRegion.this);
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
            setMask(new RectangularMask(config.getLayout().getDimension("size")));
        }

        @Override
        protected void renderContents() {
            assertRenderable();
            label.setText(printer.getLocalization(event));
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
