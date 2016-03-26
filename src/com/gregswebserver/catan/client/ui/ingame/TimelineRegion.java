package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.ChevronMask;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

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

    private final Map<Team, GraphicSet> eventGraphics;

    private final List<GameEvent> events;
    private final PlayerPool teams;
    private final TeamColors teamColors;

    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;

    public TimelineRegion(GameManager manager, TeamColors teamColors) {
        super(2, "timeline");
        //Load layout information
        eventGraphics = new EnumMap<>(Team.class);
        //Store instance information
        this.events = manager.getEvents();
        this.teams = manager.getLocalGame().getTeams();
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
        for (Team team : Team.values())
            eventGraphics.put(team,new GraphicSet(getConfig().getLayout(), "source", ChevronMask.class, teamColors.getSwaps(team)));
    }

    public void update() {
        timeline.update();
    }

    private class EventList extends ScrollingScreenRegion {
        private int lastCount;

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
        protected void renderContents() {
            clear();
            int width = 1;
            int eventSpacing = getConfig().getLayout().getInt("spacing");
            int eventBuffer = getConfig().getLayout().getInt("buffer");
            for (int i = 0; i < events.size(); i++) {
                add(new EventListElement(i)).setPosition(new Point(width, 0));
                width += eventSpacing;
            }
            width += eventBuffer;
            setMask(new RectangularMask(new Dimension(width, getConfig().getLayout().getInt("height"))));
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
                Team team = event.getOrigin() == null ? Team.None : teams.getPlayer(event.getOrigin()).getTeam();
                setGraphic(eventGraphics.get(team).getGraphic(event.getType()));
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
                return new UserEvent(this, UserEventType.History_Clicked, index);
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
        int eventBuffer = getConfig().getLayout().getInt("scroll.list.buffer");
        Insets viewInsets = new Insets(0, eventBuffer, 0, eventBuffer);
        timeline.setInsets(viewInsets);
    }

    @Override
    public String toString() {
        return "TimelineRegion";
    }
}
