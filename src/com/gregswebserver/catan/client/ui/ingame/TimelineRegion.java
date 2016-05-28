package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.game.GameEvent;
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

    private final Map<TeamColor, GraphicSet> eventGraphics;

    private final ContextRegion context;
    private final List<GameEvent> events;
    private final PlayerPool players;
    private final TeamColors teamColors;

    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;
    private int eventBuffer;

    public TimelineRegion(ContextRegion context, GameManager manager, TeamColors teamColors) {
        super(1, "timeline");
        //Load layout information
        this.context = context;
        eventGraphics = new EnumMap<>(TeamColor.class);
        //Store instance information
        this.events = manager.getEvents();
        this.players = manager.getLocalGame().getPlayers();
        this.teamColors = teamColors;
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

    @Override
    public void loadConfig(UIConfig config) {
        eventBuffer = getConfig().getLayout().getInt("scroll.list.buffer");
        for (TeamColor teamColor : TeamColor.values())
            eventGraphics.put(teamColor,new GraphicSet(config.getLayout(), "source", teamColors.getSwaps(teamColor)));
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
                context.target(index);
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
        Insets viewInsets = new Insets(0, eventBuffer, 0, eventBuffer);
        timeline.setInsets(viewInsets);
    }

    @Override
    public String toString() {
        return "TimelineRegion";
    }
}
