package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by greg on 3/11/16.
 * A game timeline allowing the user to view the progression of the game, and manually step through the history of the game.
 */
public class TimelineRegion extends ConfigurableScreenRegion {

    //Required instance details
    private final List<GameEvent> events;
    private final PlayerPool players;

    //Optional interactions
    private ContextRegion context;

    //Configuration dependencies
    private Map<TeamColor, GraphicSet> eventGraphics;

    //Sub-regions
    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;

    public TimelineRegion(GameManager manager) {
        super(1, "timeline");
        //Store instance information
        this.events = manager.getEvents();
        this.players = manager.getLocalGame().getPlayers();
        //Create sub-regions
        background = new TiledBackground(0, "background");
        timeline = new ScrollingScreenContainer(1, "scroll", new EventList()) {
            @Override
            public String toString() {
                return "TimelineScrollContainer";
            }
        };
        //Add everything to the screen.
        add(background).setClickable(this);
        add(timeline);
        timeline.setTransparency(true);
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

    public void update() {
        timeline.update();
    }

    private class EventList extends ScrollingScreenRegion {
        private int lastCount;
        private int eventSpacing;
        private int eventBuffer;
        private int eventHeight;

        private EventList() {
            super(0, "list");
            lastCount = events.size();
            setTransparency(true);
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
            setInsets(new Insets(0, eventBuffer, 0, eventBuffer));
        }

        @Override
        protected void renderContents() {
            clear();
            int width = 1;
            for (int i = 0; i < events.size(); i++) {
                add(new EventListElement(i)).setPosition(new Point(width, 0));
                width += eventSpacing;
            }
            width += eventBuffer;
            setMask(new RectangularMask(new Dimension(width, eventHeight)));
            //Scroll the bar over as new events come in.
            scroll(-eventSpacing * (events.size() - lastCount), 0);
            lastCount = events.size();
        }

        private class EventListElement extends GraphicObject {
            private final int index;

            private EventListElement(int index) {
                super(0);
                this.index = index;
                GameEvent event = events.get(index);
                TeamColor teamColor = event.getOrigin() == null ? TeamColor.None : players.getPlayer(event.getOrigin()).getTeamColor();
                setGraphic(eventGraphics.get(teamColor).getGraphic(event.getType()));
            }

            @Override
            public String toString() {
                return "EventListElement";
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
        }

        @Override
        public String toString() {
            return "TimelineEventList";
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        timeline.setMask(mask);
        background.setMask(mask);
    }

    @Override
    public String toString() {
        return "TimelineRegion";
    }
}