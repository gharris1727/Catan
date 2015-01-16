package com.gregswebserver.catan.client.renderer.ingame;

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

    public InGameScreenRegion(Dimension size, CatanGame game) {
        super(new Point(), 0, size);
        this.game = game;
        map = new MapRegion(main, 0, size, game);
        trade = new TradeRegion(side, 1, size);
        inventory = new InventoryRegion(bottom, 2, size, game.getLocalPlayer());
        context = new ContextRegion(corner, 3, size);
        add(map);
        add(trade);
        add(inventory);
        add(context);
    }

    public void setSize(Dimension d) {
        int[] widths = new int[]{d.width - sidebarWidth, sidebarWidth};
        int[] heights = new int[]{d.height - bottomHeight, bottomHeight};
        super.setGridSize(widths, heights);
        map.setSize(getCellDimension(main));
        trade.setSize(getCellDimension(side));
        inventory.setSize(getCellDimension(bottom));
        context.setSize(getCellDimension(corner));
    }

    public String toString() {
        return "InGameScreen " + game;
    }

    public void update() {
        map.forceRender();
    }
}
