package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreenRegion extends ClientScreen {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;

    private final CatanGame game;
    private final MapRegion map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;

    public InGameScreenRegion(Client client) {
        super(client);
        this.game = client.getActiveGame();
        map = new MapRegion(0, game);
        trade = new TradeRegion(1);
        inventory = new InventoryRegion(2, game.getTeams().getLocalPlayer());
        context = new ContextRegion(3);
        add(map);
        add(trade);
        add(inventory);
        add(context);
    }

    @Override
    public void update() {
        map.update();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - bottomHeight;
        trade.setPosition(new Point(mainWidth,0));
        inventory.setPosition(new Point(0,mainHeight));
        context.setPosition(new Point(mainWidth,mainHeight));
        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, mainHeight)));
        inventory.setMask(new RectangularMask(new Dimension(mainWidth, bottomHeight)));
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, bottomHeight)));
    }

    public String toString() {
        return "InGameScreenRegion";
    }
}
