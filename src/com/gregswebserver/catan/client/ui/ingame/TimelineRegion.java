package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

/**
 * Created by greg on 3/11/16.
 * A game timeline allowing the user to view the progression of the game, and manually step through the history of the game.
 */
public class TimelineRegion extends ScrollingScreenContainer {

    private static final int timelineHeight;
    private static final int eventSpacing;
    private static final int eventBuffer;

    static {
        timelineHeight = Client.graphicsConfig.getInt("interface.ingame.timeline.height");
        eventSpacing = Client.graphicsConfig.getInt("interface.ingame.timeline.spacing");
        eventBuffer = Client.graphicsConfig.getInt("interface.ingame.timeline.buffer");
    }

    private final TiledBackground background;

    public TimelineRegion(int priority, GameManager manager, TeamGraphics graphics) {
        super(priority, new EventList(1,manager,graphics), new Insets(0,eventBuffer,0,eventBuffer));
        background = new TiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "TimelineRegionBackground";
            }
        };
        add(background).setClickable(this);
    }

    private static class EventList extends ScrollingScreenRegion {

        private final TeamGraphics graphics;
        private final List<GameEvent> events;
        private final PlayerPool teams;
        private int lastCount;

        private EventList(int priority, GameManager manager, TeamGraphics graphics) {
            super(priority);
            this.graphics = graphics;
            this.events = manager.getEvents();
            this.teams = manager.getLocalGame().getTeams();
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
            for (int i = 0; i < events.size(); i++) {
                add(new EventListElement(0, i)).setPosition(new Point(width, 0));
                width += eventSpacing;
            }
            width += eventBuffer;
            setMask(new RectangularMask(new Dimension(width, timelineHeight)));
            //Scroll the bar over as new events come in.
            scroll(-eventSpacing * (events.size() - lastCount), 0);
            lastCount = events.size();
        }

        private class EventListElement extends GraphicObject {
            private final int index;

            protected EventListElement(int priority, int index) {
                super(priority);
                this.index = index;
                GameEvent event = events.get(index);
                Team team = event.getOrigin() == null ? Team.None : teams.getPlayer(event.getOrigin()).getTeam();
                setGraphic(graphics.getEventGraphic(team, event));
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
        super.resizeContents(mask);
        background.setMask(mask);
    }

    @Override
    public String toString() {
        return "TimelineRegion";
    }
}
