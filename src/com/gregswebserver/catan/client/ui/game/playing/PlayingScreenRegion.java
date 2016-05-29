package com.gregswebserver.catan.client.ui.game.playing;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.InventoryRegion;
import com.gregswebserver.catan.client.ui.game.TimelineRegion;
import com.gregswebserver.catan.client.ui.game.map.MapRegion;
import com.gregswebserver.catan.client.ui.game.map.TeamColors;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class PlayingScreenRegion extends ClientScreen {

    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final TimelineRegion timeline;
    private int sidebarWidth;
    private int inventoryHeight;
    private int contextHeight;
    private int timelineHeight;
    private Dimension borderBuffer;

    public PlayingScreenRegion(Username username, GameManager manager, TeamColors teamColors) {
        super("playing");
        //Load relevant details
        CatanGame game = manager.getLocalGame();
        context = new ContextRegion(manager, username);
        map = new ScrollingScreenContainer(0, "scroll", new MapRegion(context, game.getBoard(), teamColors)) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        trade = new TradeRegion(context, game, username);
        inventory = new InventoryRegion(game, username);
        timeline = new TimelineRegion(context, manager, teamColors);
        //Add everything to the screen
        add(map);
        add(trade);
        add(inventory);
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
        inventory.forceRender();
        trade.update();
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
        int mainWidth = mask.getWidth() - sidebarWidth;
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        inventory.setPosition(new Point(timelineWidth, 0));
        trade.setPosition(new Point(timelineWidth, inventoryHeight));
        context.setPosition(new Point(timelineWidth, inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));

        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
        inventory.setMask(new RectangularMask(new Dimension(sidebarWidth, inventoryHeight)));

        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        Insets viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
        map.setInsets(viewInsets);
        map.center();
    }

    public String toString() {
        return "PlayingScreenRegion";
    }
}
