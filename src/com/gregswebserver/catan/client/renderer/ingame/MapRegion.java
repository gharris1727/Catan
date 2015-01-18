package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.BoardObject;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.resources.GraphicsConfig;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * The region of the screen responsible for rendering the game map.
 */
public class MapRegion extends ScreenRegion {

    private final CatanGame game;
    private final RenderMask boardSize;
    private final Point mapOffset;
    private final ScreenRegion background;
    private final ScreenRegion midground;
    private final ScreenRegion foreground;

    public MapRegion(Point position, int priority, RenderMask mask, CatanGame game) {
        super(position, priority, mask);
        this.game = game;
        boardSize = new RectangularMask(GraphicsConfig.boardToScreen(game.getBoardSize()));
        this.mapOffset = new Point();
        //TODO: FIX ME.
        background = null; // new Background(mapOffset, 0, boardSize, this, ResourceLoader.getGraphic(oceanBackground));
        midground = new MiddleGround(mapOffset, 1, boardSize, game.getBoard());
        foreground = new Foreground(mapOffset, 2, boardSize);
        add(background).setClickable(this);
        add(midground).setClickable(this);
        add(foreground).setClickable(this);
    }

    private void limitBackdropScroll() {
        int maxX = -GraphicsConfig.mapEdgeBufferSize.width;
        int maxY = -GraphicsConfig.mapEdgeBufferSize.height;
        int minX = -boardSize.getWidth() + getMask().getWidth() - 2 * maxX;
        int minY = -boardSize.getHeight() + getMask().getHeight() - 2 * maxY;
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

    public UserEvent onMouseDrag(Point p) {
        mapOffset.translate(p.x, p.y);
        limitBackdropScroll();
        return null;
    }

    public void resizeComponents(RenderMask mask) {
        limitBackdropScroll();
    }

    public String toString() {
        return "MapScreenArea " + game;
    }

    private class Background extends TiledBackground {

        public Background(Point position, int priority, RenderMask mask, GraphicSet style) {
            super(position, priority, mask, style);
        }

        public String toString() {
            return "Map Background";
        }
    }

    private class MiddleGround extends ScreenRegion {

        private HashMap<Coordinate, MapScreenObject> tiles;
        private HashMap<Coordinate, MapScreenObject> roads;
        private HashMap<Coordinate, MapScreenObject> towns;

        private GameBoard board;

        public MiddleGround(Point position, int priority, RenderMask mask, GameBoard board) {
            super(position, priority, mask);
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

        public void clear() {
            tiles = new HashMap<>();
            roads = new HashMap<>();
            towns = new HashMap<>();
            super.clear();
        }

        protected void renderContents() {
            clear();
            HashMap<Coordinate, Tile> tiles = board.getTileMap();
            HashMap<Coordinate, Path> paths = board.getPathMap();
            HashMap<Coordinate, Town> towns = board.getTownMap();

            for (Map.Entry<Coordinate, Tile> e : tiles.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(GraphicsConfig.tileToScreen(c), 0, c, e.getValue());
                add(o);
            }
            for (Map.Entry<Coordinate, Path> e : paths.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(GraphicsConfig.edgeToScreen(c), 1, c, e.getValue());
                add(o);
            }
            for (Map.Entry<Coordinate, Town> e : towns.entrySet()) {
                Coordinate c = e.getKey();
                ScreenObject o = new MapScreenObject(GraphicsConfig.vertexToScreen(c), 2, c, e.getValue());
                add(o);
            }
        }

        public String toString() {
            return "Middle Ground";
        }

        private class MapScreenObject extends StaticObject {

            private final Coordinate coordinate;
            private final BoardObject object;

            public MapScreenObject(Point position, int priority, Coordinate coordinate, BoardObject object) {
                super(position, priority, object.getGraphic());
                this.coordinate = coordinate;
                this.object = object;
            }

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

        public Foreground(Point position, int priority, RenderMask mask) {
            super(position, priority, mask);
        }

        public String toString() {
            return "Foreground";
        }
    }
}
