package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreenRegion extends GridScreenRegion {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;
    private final static Point main = new Point(0, 0);
    private final static Point side = new Point(1, 0);
    private final static Point bottom = new Point(0, 1);
    private final static Point corner = new Point(1, 1);

    private final CatanGame game;
    private final MapRegion map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;

    public InGameScreenRegion(RenderMask mask, CatanGame game) {
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

    public void setMask(RenderMask mask) {
        int[] widths = new int[]{mask.getWidth() - sidebarWidth, sidebarWidth};
        int[] heights = new int[]{mask.getHeight() - bottomHeight, bottomHeight};
        super.setGridSize(widths, heights, mask);
        map.setMask(new RectangularMask(getCellDimension(main)));
        trade.setMask(new RectangularMask(getCellDimension(side)));
        inventory.setMask(new RectangularMask(getCellDimension(bottom)));
        context.setMask(new RectangularMask(getCellDimension(corner)));
    }

    public String toString() {
        return "InGameScreen " + game;
    }

    public void update() {
        map.forceRender();
    }
}
