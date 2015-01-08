package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.screen.GridObjectArea;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameObject extends GridObjectArea {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;
    private final static Point main = new Point(0, 0);
    private final static Point side = new Point(1, 0);
    private final static Point bottom = new Point(0, 1);
    private final static Point corner = new Point(1, 1);

    private final CatanGame game;
    private final MapObjectArea map;
    private final TradeObjectArea trade;
    private final InventoryObjectArea inventory;
    private final ContextObjectArea context;

    public InGameObject(CatanGame game) {
        super(new Point(), 0);
        this.game = game;
        map = new MapObjectArea(main, 0, game.getBoard());
        trade = new TradeObjectArea(side, 1);
        inventory = new InventoryObjectArea(bottom, 2, game.getLocalPlayer());
        context = new ContextObjectArea(corner, 3);
        add(map);
        add(trade);
        add(inventory);
        add(context);
    }

    public void setSize(Dimension d) {
        int[] widths = new int[]{d.width - sidebarWidth, sidebarWidth};
        int[] heights = new int[]{d.height - bottomHeight, bottomHeight};
        super.resize(widths, heights);
        map.setSize(getCellDimension(main));
        trade.setSize(getCellDimension(side));
        inventory.setSize(getCellDimension(bottom));
        context.setSize(getCellDimension(corner));
    }

    public void scroll(Point p) {
        map.scroll(p);
    }

    public String toString() {
        return "InGameScreen " + game;
    }

}
