package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.screen.*;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.resources.GraphicsConfig;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import static com.gregswebserver.catan.client.resources.GraphicInfo.OceanBackground;

/**
 * Created by Greg on 1/5/2015.
 * The region of the screen responsible for rendering the game map.
 */
public class MapObjectArea extends ColorObjectArea {

    private final CatanGame game;
    private final Dimension boardSize;
    private final Point mapOffset;
    private final ObjectArea background;
    private final ObjectArea midground;
    private final ObjectArea foreground;

    public MapObjectArea(Point position, int priority, CatanGame game) {
        super(position, priority);
        this.game = game;
        boardSize = GraphicsConfig.boardToScreen(game.getBoardSize());
        this.mapOffset = new Point();
        background = new Background(mapOffset, 0, ResourceLoader.getGraphic(OceanBackground));
        midground = new MiddleGround(mapOffset, 1, game.getBoard());
        foreground = new Foreground(mapOffset, 2);
        background.setSize(boardSize);
        midground.setSize(boardSize);
        foreground.setSize(boardSize);
        add(background);
        add(midground);
        add(foreground);
    }

    public UserEvent onMouseDrag(Point p) {
        return new UserEvent(this, UserEventType.Map_Drag, p);
    }

    public void scroll(Point p) {
        mapOffset.translate(p.x, p.y);
        limitBackdropScroll();
    }

    public void setSize(Dimension d) {
        super.setSize(d);
        limitBackdropScroll();
    }

    private void limitBackdropScroll() {
        int maxX = -GraphicsConfig.mapEdgeBufferSize.width;
        int maxY = -GraphicsConfig.mapEdgeBufferSize.height;
        int minX = -boardSize.width + getSize().width - 2 * maxX;
        int minY = -boardSize.height + getSize().height - 2 * maxY;
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

    public String toString() {
        return "MapScreenArea " + game;
    }

    private class Background extends TiledArea {

        public Background(Point position, int priority, Graphic texture) {
            super(position, priority, texture);
            setClickable(MapObjectArea.this);
        }

        public String toString() {
            return "Map Background";
        }
    }

    private class MiddleGround extends ColorObjectArea {

        private GameBoard board;

        public MiddleGround(Point position, int priority, GameBoard board) {
            super(position, priority);
            this.board = board;
        }

        protected void render() {
            clear();
            HashMap<Coordinate, Tile> tiles = board.getTileMap();
            HashMap<Coordinate, Path> paths = board.getPathMap();
            HashMap<Coordinate, Town> towns = board.getTownMap();

            for (Map.Entry<Coordinate, Tile> e : tiles.entrySet()) {
                Coordinate c = e.getKey();
                Tile t = e.getValue();
                Graphic g = t.getGraphic();
                ScreenObject o = new StaticObject(GraphicsConfig.tileToScreen(c), 0, g) {
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Tile_Clicked, c);
                    }

                    public UserEvent onMouseDrag(Point p) {
                        return MapObjectArea.this.onMouseDrag(p);
                    }

                    public String toString() {
                        return "Map Tile " + c;
                    }
                };
                add(o);
            }
            for (Map.Entry<Coordinate, Path> e : paths.entrySet()) {
                Coordinate c = e.getKey();
                Path p = e.getValue();
                Graphic g = p.getGraphic();
                ScreenObject o = new StaticObject(GraphicsConfig.edgeToScreen(c), 2, g) {

                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Edge_Clicked, c);
                    }

                    public UserEvent onMouseDrag(Point p) {
                        return MapObjectArea.this.onMouseDrag(p);
                    }

                    public String toString() {
                        return "Map Path " + c;
                    }
                };
                add(o);
            }
            for (Map.Entry<Coordinate, Town> e : towns.entrySet()) {
                Coordinate c = e.getKey();
                Town t = e.getValue();
                Graphic g = t.getGraphic();
                ScreenObject o = new StaticObject(GraphicsConfig.vertexToScreen(c), 3, g) {

                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Vertex_Clicked, c);
                    }

                    public UserEvent onMouseDrag(Point p) {
                        return MapObjectArea.this.onMouseDrag(p);
                    }

                    public String toString() {
                        return "Map Town " + c;
                    }
                };
                add(o);
            }
        }

        public String toString() {
            return "Middle Ground";
        }
    }

    private class Foreground extends ColorObjectArea {

        public Foreground(Point position, int priority) {
            super(position, priority);
        }

        public String toString() {
            return "Foreground";
        }
    }
}
