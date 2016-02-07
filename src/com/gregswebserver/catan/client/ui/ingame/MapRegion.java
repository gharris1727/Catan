package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenRegion;
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
public class MapRegion extends UIScreenRegion {

    private static final Dimension unitSize = Client.staticConfig.getDimension("catan.graphics.tiles.unit.size");
    private static final Dimension borderBuffer = Client.staticConfig.getDimension("catan.graphics.interface.ingame.borderbuffer");
    private static final Insets viewInsets = new Insets(borderBuffer.height,borderBuffer.width,borderBuffer.height,borderBuffer.width);

    private final CatanGame game;
    private final BoardArea boardArea;

    public MapRegion(int priority, CatanGame game) {
        super(priority);
        this.game = game;
        boardArea = new BoardArea(1, game.getBoard());
        add(boardArea);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        boardArea.setHostView(mask, viewInsets);
    }

    public void update() {
        boardArea.forceRender();
    }

    @Override
    public String toString() {
        return "MapRegion";
    }

    private class BoardArea extends ScrollingScreenRegion {

        private HashMap<Coordinate, MapScreenObject> tiles;
        private HashMap<Coordinate, MapScreenObject> roads;
        private HashMap<Coordinate, MapScreenObject> towns;

        private final GameBoard board;

        private final TiledBackground background;

        public BoardArea(int priority, GameBoard board) {
            super(priority);
            this.board = board;
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_GAME) {
                public String toString() {
                    return "BoardAreaBackground";
                }
            };
            add(background).setClickable(this);
            setPosition(new Point());
            setMask(new RectangularMask(boardToScreen(game.getBoardSize())));
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
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            clear();
            HashMap<Coordinate, Tile> tiles = board.getTileMap();
            HashMap<Coordinate, Path> paths = board.getPathMap();
            HashMap<Coordinate, Town> towns = board.getTownMap();

            for (Map.Entry<Coordinate, Tile> e : tiles.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(0, c, e.getValue());
                add(o);
            }
            for (Map.Entry<Coordinate, Path> e : paths.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(1, c, e.getValue());
                //add(o);
                //TODO: unfuck the diagonal mask so this doesnt break everything.
            }
            for (Map.Entry<Coordinate, Town> e : towns.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(2, c, e.getValue());
                add(o);
            }
            add(background);
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            scroll(p.x, p.y);
            return null;
        }

        public String toString() {
            return "BoardArea";
        }

        private class MapScreenObject extends GraphicObject {

            private final Coordinate coordinate;
            private final BoardObject object;

            public MapScreenObject(int priority, Coordinate coordinate, BoardObject object) {
                super(priority, object.getGraphic());
                this.coordinate = coordinate;
                this.object = object;
                if (object instanceof Tile)
                    setPosition(tileToScreen(coordinate));
                if (object instanceof Road)
                    setPosition(edgeToScreen(coordinate));
                if (object instanceof Town)
                    setPosition(vertexToScreen(coordinate));
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

            @Override
            public UserEvent onMouseDrag(Point p) {
                return BoardArea.this.onMouseDrag(p);
            }

            public String toString() {
                return "MapScreenObject C: " + coordinate + " O: " + object;
            }

        }
    }

    private static final int[][] tileOffsets = {
            {12, 112}, //Horizontal
            {16, 72}}; //Vertical
    private static final int[][] edgeOffsets = {
            {0, 0, 36, 100, 100, 136}, //Horizontal
            {9, 65, 0, 65, 9, 56}}; //Vertical
    private static final int[][] vertOffsets = {
            {0, 24, 100, 124}, //Horizontal
            {56, 0, 0, 56}}; //Vertical

    private static Dimension boardToScreen(Dimension size) {
        int outW = ((size.width + 1) / 2) * unitSize.width;
        int outH = (size.height + 1) * unitSize.height;
        return new Dimension(outW, outH);
    }

    private static Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    private static Point edgeToScreen(Coordinate c) {
        int outX = (c.x / 6) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    private static Point vertexToScreen(Coordinate c) {
        int outX = (c.x / 4) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += vertOffsets[0][c.x % 4];
        outY += vertOffsets[1][c.x % 4];
        return new Point(outX, outY);
    }
}
