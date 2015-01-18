package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreenRegion extends ScreenRegion {

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

    public InGameScreenRegion(RenderMask mask, UIStyle style, CatanGame game) {
        super(new Point(), 0, mask);
        this.game = game;
        map = new MapRegion(main, 0, mask, game);
        trade = new TradeRegion(side, 1, mask);
        inventory = new InventoryRegion(bottom, 2, mask, game.getLocalPlayer());
        context = new ContextRegion(corner, 3, mask);
        add(map);
        add(trade);
        add(inventory);
        add(context);
    }

    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - bottomHeight;
        side.x = mainWidth;
        bottom.y = mainHeight;
        corner.x = mainWidth;
        corner.y = mainHeight;
        map.resize(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        trade.resize(new RectangularMask(new Dimension(sidebarWidth, mainHeight)));
        inventory.resize(new RectangularMask(new Dimension(mainWidth, bottomHeight)));
        context.resize(new RectangularMask(new Dimension(sidebarWidth, bottomHeight)));
    }

    public String toString() {
        return "InGameScreen " + game;
    }

    public void update() {
        map.forceRender();
    }
}
