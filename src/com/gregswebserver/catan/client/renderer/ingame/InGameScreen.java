package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.areas.GridScreenArea;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreen extends GridScreenArea {

    private final static int sidebarWidth = 256;
    private final static int bottomHeight = 256;
    private final static Point main = new Point(0, 0);
    private final static Point side = new Point(1, 0);
    private final static Point bottom = new Point(0, 1);
    private final static Point corner = new Point(1, 1);

    private CatanGame game;
    private MapScreenArea map;
    private TradeScreenArea trade;
    private InventoryScreenArea inventory;
    private ContextScreenArea context;

    public InGameScreen(CatanGame game) {
        super(new Point(), 0);
        this.game = game;
        map = new MapScreenArea(main, 0, game.getBoard());
        add(map);
        trade = new TradeScreenArea(side, 1);
        add(trade);
        inventory = new InventoryScreenArea(bottom, 2, game.getLocalPlayer());
        add(inventory);
        context = new ContextScreenArea(corner, 3);
        add(context);
    }

    public void resize(Dimension d) {
        int[] widths = new int[]{d.width - sidebarWidth, sidebarWidth};
        int[] heights = new int[]{d.height - bottomHeight, bottomHeight};
        super.resize(widths, heights);
        map.resize(getCellDimension(main));
        trade.resize(getCellDimension(side));
        inventory.resize(getCellDimension(bottom));
        context.resize(getCellDimension(corner));
    }

    public void update() {
        map.update();
    }

    public void scroll(Point p) {
        map.scroll(p);
    }

    private class TradeScreenArea extends GridScreenArea {

        public TradeScreenArea(Point position, int priority) {
            super(position, priority);
        }

        public void resize(Dimension d) {

        }

        protected void render() {

        }
    }

    private class ContextScreenArea extends GridScreenArea {

        public ContextScreenArea(Point position, int priority) {
            super(position, priority);
        }

        public void resize(Dimension d) {

        }

        protected void render() {

        }
    }

}
