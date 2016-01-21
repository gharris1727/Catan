package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.BoardObject;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * The region of the screen responsible for rendering the game map.
 */
public class MapRegion extends ScreenRegion {

    private static final Dimension unitSize = Main.staticConfig.getDimension("catan.graphics.tiles.unit.size");
    private static final Dimension borderBuffer = Main.staticConfig.getDimension("catan.graphics.interface.ingame.borderbuffer");
    
    private final CatanGame game;
    private final RenderMask boardSize;

    private final ScreenRegion background;
    private final ScreenRegion midground;
    private final ScreenRegion foreground;

    public MapRegion(int priority, CatanGame game) {
        super(priority);
        this.game = game;
        this.boardSize = new RectangularMask(boardToScreen(game.getBoardSize()));
        background =  new Background(0);
        midground = new MiddleGround(1, game.getBoard());
        foreground = new Foreground(2);
        add(background).setClickable(this);
        add(midground).setClickable(this);
        add(foreground).setClickable(this);
    }

    @Override
    protected boolean limitScroll() {
        int maxX = -borderBuffer.width;
        int maxY = -borderBuffer.height;
        int minX = -boardSize.getWidth() + getMask().getWidth() - 2 * maxX;
        int minY = -boardSize.getHeight() + getMask().getHeight() - 2 * maxY;
        boolean changed = false;
        Point mapOffset =  midground.getPosition();
        if (mapOffset.x < minX) {
            changed = true;
            mapOffset.x = minX;
        }
        if (mapOffset.y < minY){
            changed = true;
            mapOffset.y = minY;
        }
        if (mapOffset.x > maxX){
            changed = true;
            mapOffset.x = maxX;
        }
        if (mapOffset.y > maxY){
            changed = true;
            mapOffset.y = maxY;
        }
        return changed;
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        midground.getPosition().translate(p.x, p.y);
        return null;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        limitScroll();
        background.setMask(mask);
        foreground.setMask(mask);
    }

    @Override
    protected void renderContents() {
    }

    @Override
    public String toString() {
        return "MapScreenArea " + game;
    }

    private class Background extends TiledBackground {

        public Background(int priority) {
            super(priority, UIStyle.BACKGROUND_GAME);
        }

        public String toString() {
            return "Map Background";
        }
    }

    private class MiddleGround extends ScreenRegion {

        private HashMap<Coordinate, MapScreenObject> tiles;
        private HashMap<Coordinate, MapScreenObject> roads;
        private HashMap<Coordinate, MapScreenObject> towns;

        private final GameBoard board;

        public MiddleGround(int priority, GameBoard board) {
            super(priority);
            this.board = board;
        }

        public ScreenObject add(MapScreenObject object) {
            if (object == null) return null;
            if (object.object instanceof Tile)
                tiles.put(object.coordinate, object);
            if (object.object instanceof Road)
                roads.put(object.coordinate, object);
            if (object.object instanceof Town)
                towns.put(object.coordinate, object);
            return super.add(object);
        }

        public ScreenObject remove(MapScreenObject object) {
            if (object == null) return null;
            if (object.object instanceof Tile)
                tiles.remove(object.coordinate);
            if (object.object instanceof Road)
                roads.remove(object.coordinate);
            if (object.object instanceof Town)
                towns.remove(object.coordinate);
            return super.remove(object);
        }

        @Override
        public void clear() {
            tiles = new HashMap<>();
            roads = new HashMap<>();
            towns = new HashMap<>();
            super.clear();
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }

        @Override
        protected void renderContents() {
            clear();
            HashMap<Coordinate, Tile> tiles = board.getTileMap();
            HashMap<Coordinate, Path> paths = board.getPathMap();
            HashMap<Coordinate, Town> towns = board.getTownMap();

            for (Map.Entry<Coordinate, Tile> e : tiles.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(tileToScreen(c), 0, c, e.getValue());
                add(o);
            }
            for (Map.Entry<Coordinate, Path> e : paths.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(edgeToScreen(c), 1, c, e.getValue());
                add(o);
            }
            for (Map.Entry<Coordinate, Town> e : towns.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(vertexToScreen(c), 2, c, e.getValue());
                add(o);
            }
        }

        public String toString() {
            return "Middle Ground";
        }

        private class MapScreenObject extends GraphicObject {

            private final Coordinate coordinate;
            private final BoardObject object;

            public MapScreenObject(Point position, int priority, Coordinate coordinate, BoardObject object) {
                super(priority, object.getGraphic());
                this.coordinate = coordinate;
                this.object = object;
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                if (object instanceof Tile)
                    return new UserEvent(this, UserEventType.Tile_Clicked, coordinate);
                if (object instanceof Road)
                    return new UserEvent(this, UserEventType.Edge_Clicked, coordinate);
                if (object instanceof Town)
                    return new UserEvent(this, UserEventType.Vertex_Clicked, coordinate);
                return null;
            }


            public String toString() {
                return "MapScreenObject C: " + coordinate + " O: " + object;
            }

        }
    }

    private class Foreground extends ScreenRegion {

        public Foreground(int priority) {
            super(priority);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }

        public String toString() {
            return "Foreground";
        }
    }

    public static final int[][] tileOffsets = {
            {12, 112}, //Horizontal
            {16, 72}}; //Vertical
    public static final int[][] edgeOffsets = {
            {0, 0, 36, 100, 100, 136}, //Horizontal
            {9, 65, 0, 65, 9, 56}}; //Vertical
    public static final int[][] vertOffsets = {
            {0, 24, 100, 124}, //Horizontal
            {56, 0, 0, 56}}; //Vertical

    public static Dimension boardToScreen(Dimension size) {
        int outW = ((size.width + 1) / 2) * unitSize.width;
        int outH = (size.height + 1) * unitSize.height;
        return new Dimension(outW, outH);
    }

    public static Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    public static Point edgeToScreen(Coordinate c) {
        int outX = (c.x / 6) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    public static Point vertexToScreen(Coordinate c) {
        int outX = (c.x / 4) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += vertOffsets[0][c.x % 4];
        outY += vertOffsets[1][c.x % 4];
        return new Point(outX, outY);
    }
}
