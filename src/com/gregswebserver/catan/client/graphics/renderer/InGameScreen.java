package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.client.graphics.areas.GridScreenArea;
import com.gregswebserver.catan.client.graphics.areas.StaticGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.util.GraphicsConfig;
import com.gregswebserver.catan.common.util.Statics;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        clickable = new InGameClickable();
        this.game = game;
        map = new MapScreenArea(main, 0);
        add(map);
        trade = new TradeScreenArea(side, 1);
        add(trade);
        inventory = new InventoryScreenArea(bottom, 2);
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

    private class InGameClickable implements Clickable {
    }

    private class MapScreenArea extends ColorScreenArea {

        private final GameBoard board;
        private final Point mapOffset;
        private final Backdrop backdrop;

        public MapScreenArea(Point position, int priority) {
            super(position, priority);
            clickable = new MapScreenClickable();
            this.board = game.getBoard();
            this.mapOffset = new Point();
            backdrop = new Backdrop(mapOffset, 0);
            backdrop.resize(GraphicsConfig.boardToScreen(board.size));
            add(backdrop);
        }

        public void scroll(Point p) {
            mapOffset.translate(p.x, p.y);
            limitBackdropScroll();
        }

        public void resize(Dimension d) {
            super.resize(d);
            limitBackdropScroll();
        }

        private void limitBackdropScroll() {
            int maxX = -GraphicsConfig.mapEdgeBufferSize.width;
            int maxY = -GraphicsConfig.mapEdgeBufferSize.height;
            int minX = -backdrop.getSize().width + size.width - 2 * maxX;
            int minY = -backdrop.getSize().height + size.height - 2 * maxY;
            if (mapOffset.x < minX)
                mapOffset.x = minX;
            if (mapOffset.y < minY)
                mapOffset.y = minY;
            if (mapOffset.x > maxX)
                mapOffset.x = maxX;
            if (mapOffset.y > maxY)
                mapOffset.y = maxY;
            forceRender();
        }

        public void update() {
            backdrop.forceRender();
        }

        private class Backdrop extends ColorScreenArea {

            public Backdrop(Point position, int priority) {
                super(position, priority);
            }

            protected void render() {
                clear();
                synchronized (board) {
                    HashSet<Coordinate> spaces = board.hexArray.spaces.getAllCoordinates();
                    HashSet<Coordinate> edges = board.hexArray.edges.getAllCoordinates();
                    HashSet<Coordinate> vertices = board.hexArray.vertices.getAllCoordinates();

                    HashMap<Coordinate, Tile> tiles = board.hexArray.spaces.toHashMap();
                    HashMap<Coordinate, Path> paths = board.hexArray.edges.toHashMap();
                    HashMap<Coordinate, Building> buildings = board.hexArray.vertices.toHashMap();

                    for (Coordinate c : spaces) {
                        Tile t = tiles.get(c);
                        Graphic g = t.getGraphic();
                        Clickable clickable = new Clickable() {

                            private Coordinate coordinate = c;
                            private Tile tile = t;

                            //TODO: implement tile clicking
                            public void onMouseDrag(Point p) {
                                Backdrop.this.clickable.onMouseDrag(p);
                            }

                        };
                        add(new StaticGraphic(GraphicsConfig.tileToScreen(c), c.hashCode(), g, clickable));
                    }
                    for (Coordinate c : edges) {
                        Path p = paths.get(c);
                        Graphic g = null;
                        if (p != null) g = p.getGraphic();
                        if (g == null) g = Team.Blank.paths[c.x % 3];
                        Clickable clickable = new Clickable() {

                            private Coordinate coordinate = c;
                            private Path path = p;

                            //TODO: implement path clicking
                            public void onMouseDrag(Point p) {
                                Backdrop.this.clickable.onMouseDrag(p);
                            }

                        };
                        add(new StaticGraphic(GraphicsConfig.edgeToScreen(c), c.hashCode(), g, clickable));
                    }
                    for (Coordinate c : vertices) {
                        Building b = buildings.get(c);
                        Graphic g = null;
                        if (b != null) g = b.getGraphic();
                        if (g == null) g = Team.Blank.settlement[c.x % 2];
                        Clickable clickable = new Clickable() {

                            private Coordinate coordinate = c;
                            private Building building = b;

                            //TODO: implement vertex clicking
                            public void onMouseDrag(Point p) {
                                Backdrop.this.clickable.onMouseDrag(p);
                            }

                        };
                        add(new StaticGraphic(GraphicsConfig.vertexToScreen(c), c.hashCode(), g, clickable));
                    }
                }
            }


        }
    }

    private class MapScreenClickable implements Clickable {
        public void onMouseDrag(Point p) {
            map.scroll(p);
        }
    }

    private class InventoryScreenArea extends ColorScreenArea {

        private Player player;

        public InventoryScreenArea(Point position, int priority) {
            super(position, priority);
            clickable = new InventoryScreenClickable();
            this.player = game.getLocalPlayer();
        }

        protected void render() {
            clear();
            //TODO: clean up this renderer
            HashMap<Tradeable, Integer> inventory = player.getInventory();
            ArrayList<Point> positions = new ArrayList<>();
            for (Tradeable t : inventory.keySet()) {
                int num = inventory.get(t);
                Graphic graphic = Statics.nullGraphic;
                if (t instanceof Graphical)
                    graphic = ((Graphical) t).getGraphic();
                Point position = new Point();
                positions.add(position);
                Clickable clickable = new Clickable() {
                    //TODO: implement inventory clickables.
                };
                for (int i = 0; i < num; i++) {
                    add(new StaticGraphic(position, 0, graphic, clickable));
                }
            }
            int divX = (getSize().width - 128) / positions.size();
            for (int i = 0; i < positions.size(); i++) {
                positions.get(i).setLocation(divX * (i + 1), 16);
            }
        }
    }

    private class InventoryScreenClickable implements Clickable {

    }

    private class TradeScreenArea extends GridScreenArea {

        public TradeScreenArea(Point position, int priority) {
            super(position, priority);
            clickable = new TradeScreenClickable();
        }

        public void resize(Dimension d) {

        }

        protected void render() {

        }
    }

    private class TradeScreenClickable implements Clickable {

    }

    private class ContextScreenArea extends GridScreenArea {

        public ContextScreenArea(Point position, int priority) {
            super(position, priority);
            clickable = new ContextScreenClickable();
        }

        public void resize(Dimension d) {

        }

        protected void render() {

        }
    }

    private class ContextScreenClickable implements Clickable {

    }

}
