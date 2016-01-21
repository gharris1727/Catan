package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.ClientScreen;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreenRegion extends ClientScreen {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;
    private final static Point main = new Point();
    private final static Point side = new Point();
    private final static Point bottom = new Point();
    private final static Point corner = new Point();

    private final CatanGame game;
    private final MapRegion map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;

    public InGameScreenRegion(Client client, CatanGame game) {
        super(client);
        this.game = game;
        map = new MapRegion(0, game);
        trade = new TradeRegion(1);
        inventory = new InventoryRegion(2, game.getLocalPlayer());
        context = new ContextRegion(3);
        add(map);
        add(trade);
        add(inventory);
        add(context);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - bottomHeight;
        side.x = mainWidth;
        bottom.y = mainHeight;
        corner.x = mainWidth;
        corner.y = mainHeight;
        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, mainHeight)));
        inventory.setMask(new RectangularMask(new Dimension(mainWidth, bottomHeight)));
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, bottomHeight)));
    }

    public String toString() {
        return "InGameScreen " + game;
    }
}
