package com.gregswebserver.catan.client.ui.game.spectate;

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
 * Screen for spectating a game in progress.
 */
public class SpectateScreenRegion extends ClientScreen {

    //Configuration dependencies
    private int sidebarWidth;
    private int contextHeight;
    private int timelineHeight;

    //Sub-regions
    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TimelineRegion timeline;

    public SpectateScreenRegion(GameManager manager) {
        super("SpectatorScreen", "spectate");
        //Load relevant details
        context = new ContextRegion();
        MapRegion mapRegion = new MapRegion(manager.getLocalGame().getBoard());
        map = new ScrollingScreenContainer("MapScroll", 0, mapRegion);
        timeline = new TimelineRegion(manager.getRemoteGame());
        context.setGameManager(manager);
        mapRegion.setContext(context);
        timeline.setContext(context);
        //Add everything to the screen
        add(map);
        add(context);
        add(timeline);
    }

    @Override
    public void update() {
        map.update();
        timeline.update();
        context.update();
    }

    @Override
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth();
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - contextHeight;

        context.setPosition(new Point(timelineWidth, tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));

        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        map.center();
    }
}
