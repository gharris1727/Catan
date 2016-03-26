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
public class TimelineRegion extends UIScreen {

    private final int eventBuffer;
    private final Map<Team, GraphicSet> eventGraphics;

    private final List<GameEvent> events;
    private final PlayerPool teams;

    private final TiledBackground background;
    private final ScrollingScreenContainer timeline;

    public TimelineRegion(UIScreen parent, GameManager manager, TeamColors teamColors) {
        super(2, parent, "timeline");
        //Load layout information
        eventBuffer = getLayout().getInt("buffer");
        eventGraphics = new EnumMap<>(Team.class);
        for (Team team : Team.values())
            eventGraphics.put(team,new GraphicSet(getLayout(), "source", ChevronMask.class, teamColors.getSwaps(team)));
        //Store instance information
        this.events = manager.getEvents();
        this.teams = manager.getLocalGame().getTeams();
        //Create sub-regions
        background = new TiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
        timeline = new ScrollingScreenContainer(1, new EventList(), new Insets(0, eventBuffer, 0, eventBuffer)) {

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

    public void update() {
        timeline.update();
    }

    private class EventList extends ScrollingScreenRegion {
        private int lastCount;

        private EventList() {
            super(0);
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
            int eventSpacing = getLayout().getInt("spacing");
            for (int i = 0; i < events.size(); i++) {
                add(new EventListElement(i)).setPosition(new Point(width, 0));
                width += eventSpacing;
            }
            width += eventBuffer;
            setMask(new RectangularMask(new Dimension(width, getLayout().getInt("height"))));
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
    }

    @Override
    public String toString() {
        return "TimelineRegion";
    }
}
