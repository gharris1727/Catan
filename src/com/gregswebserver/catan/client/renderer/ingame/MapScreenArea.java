package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.client.graphics.areas.ScreenObject;
import com.gregswebserver.catan.client.graphics.areas.StaticGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.util.GraphicsConfig;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 1/5/2015.
 * The region of the screen responsible for rendering the game map.
 */
public class MapScreenArea extends ColorScreenArea {

    private final GameBoard board;
    private final Point mapOffset;
    private final Backdrop backdrop;

    public MapScreenArea(Point position, int priority, GameBoard board) {
        super(position, priority);
        this.board = board;
        this.mapOffset = new Point();
        backdrop = new Backdrop(mapOffset, 0);
        backdrop.resize(GraphicsConfig.boardToScreen(board.size));
        add(backdrop);
    }

    public UserEvent onMouseDrag(Point p) {
        return new UserEvent(this, UserEventType.Map_Drag, p);
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
                    ScreenObject o = new StaticGraphic(GraphicsConfig.tileToScreen(c), c.hashCode(), g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Tile_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapScreenArea.this.onMouseDrag(p);
                        }

                    };
                    add(o);
                }
                for (Coordinate c : edges) {
                    Path p = paths.get(c);
                    Graphic g = null;
                    if (p != null) g = p.getGraphic();
                    if (g == null) g = Team.Blank.paths[c.x % 3];
                    ScreenObject o = new StaticGraphic(GraphicsConfig.edgeToScreen(c), c.hashCode(), g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Edge_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapScreenArea.this.onMouseDrag(p);
                        }

                    };
                    add(o);
                }
                for (Coordinate c : vertices) {
                    Building b = buildings.get(c);
                    Graphic g = null;
                    if (b != null) g = b.getGraphic();
                    if (g == null) g = Team.Blank.settlement[c.x % 2];
                    ScreenObject o = new StaticGraphic(GraphicsConfig.vertexToScreen(c), c.hashCode(), g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Vertex_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapScreenArea.this.onMouseDrag(p);
                        }

                    };
                    add(o);
                }
            }
        }


    }
}
