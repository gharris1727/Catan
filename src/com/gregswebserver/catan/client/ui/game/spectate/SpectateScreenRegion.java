package com.gregswebserver.catan.client.ui.game.spectate;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.TimelineRegion;
import com.gregswebserver.catan.client.ui.game.map.MapRegion;
import com.gregswebserver.catan.client.ui.game.map.TeamColors;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by greg on 5/28/16.
 * Screen for spectating a game in progress.
 */
public class SpectateScreenRegion extends ClientScreen {

    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TimelineRegion timeline;
    private int sidebarWidth;
    private int inventoryHeight;
    private int contextHeight;
    private int timelineHeight;
    private Dimension borderBuffer;

    public SpectateScreenRegion(GameManager manager, TeamColors teamColors) {
        super("spectate");
        //Load relevant details
        CatanGame game = manager.getLocalGame();
        context = new ContextRegion(manager, null);
        map = new ScrollingScreenContainer(0, "scroll", new MapRegion(context, game.getBoard(), teamColors)) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        timeline = new TimelineRegion(context, manager, teamColors);
        //Add everything to the screen
        add(map);
        add(context);
        add(timeline);
    }

    public void target(Object o) {
        context.target(o);
    }

    @Override
    public void refresh() {
        map.update();
        timeline.update();
        context.forceRender();
    }

    @Override
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        inventoryHeight = config.getLayout().getInt("inventory.height");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
        borderBuffer = config.getLayout().getDimension("borderbuffer");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth();
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        context.setPosition(new Point(timelineWidth, inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));

        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        Insets viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
        map.setInsets(viewInsets);
        map.center();
    }

    @Override
    public String toString() {
        return "SpectateScreenRegion";
    }
}
