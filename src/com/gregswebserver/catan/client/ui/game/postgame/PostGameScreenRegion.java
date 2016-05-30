package com.gregswebserver.catan.client.ui.game.postgame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.MapRegion;
import com.gregswebserver.catan.client.ui.game.TimelineRegion;

import java.awt.*;

/**
 * Created by greg on 5/28/16.
 * A Screen that is displayed after a game is finished.
 * Allows the players to explore the map, and view the scores that various players recieved.
 */
public class PostGameScreenRegion extends ClientScreen {

    //Configuration dependencies
    private int sidebarWidth;
    private int contextHeight;
    private int timelineHeight;

    //Sub-regions
    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TimelineRegion timeline;
    private final ScoreboardRegion scoreboard;

    public PostGameScreenRegion(GameManager manager) {
        super("postgame");
        context = new ContextRegion();
        MapRegion mapRegion = new MapRegion(manager.getLocalGame().getBoard());
        map = new ScrollingScreenContainer(0, "scroll", mapRegion) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        scoreboard = new ScoreboardRegion(manager.getLocalGame());
        timeline = new TimelineRegion(manager);
        context.setGameManager(manager);
        mapRegion.setContext(context);
        timeline.setContext(context);
        add(context);
        add(map);
        add(scoreboard);
        add(timeline);
    }

    @Override
    public void refresh() {
        map.update();
        timeline.update();
        scoreboard.forceRender();
        context.forceRender();
    }

    @Override
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - contextHeight;

        context.setPosition(new Point(timelineWidth, tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));
        scoreboard.setPosition(new Point(timelineWidth, 0));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        scoreboard.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        map.center();
    }

    public String toString() {
        return "PostGameScreenRegion";
    }
}
