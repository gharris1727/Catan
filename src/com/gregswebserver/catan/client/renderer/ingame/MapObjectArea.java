package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.screen.*;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.buildings.EmptyBuilding;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.EmptyPath;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.EmptyTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.resources.GraphicsConfig;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

import static com.gregswebserver.catan.common.resources.cached.GraphicInfo.OceanBackground;

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
        background = new TiledArea(mapOffset, 0, ResourceLoader.getGraphic(OceanBackground)) {
            public UserEvent onMouseDrag(Point p) {
                return new UserEvent(this, UserEventType.Map_Drag, p);
            }

            public String toString() {
                return "Map Background";
            }
        };
        midground = new MapElements(mapOffset, 1);
        foreground = new ColorObjectArea(mapOffset, 2) {
            public UserEvent onMouseDrag(Point p) {
                return new UserEvent(this, UserEventType.Map_Drag, p);
            }

            public String toString() {
                return "Map Foreground";
            }
        };
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

    private class MapElements extends ColorObjectArea {

        public MapElements(Point position, int priority) {
            super(position, priority);
        }

        protected void render() {
            clear();
            synchronized (game) {
                //TODO: add some kind of ocean backdrop.

                HashSet<Coordinate> spaces = game.hexArray.spaces.getAllCoordinates();
                HashSet<Coordinate> edges = game.hexArray.edges.getAllCoordinates();
                HashSet<Coordinate> vertices = game.hexArray.vertices.getAllCoordinates();

                HashMap<Coordinate, Tile> tiles = game.hexArray.spaces.toHashMap();
                HashMap<Coordinate, Path> paths = game.hexArray.edges.toHashMap();
                HashMap<Coordinate, Building> buildings = game.hexArray.vertices.toHashMap();

                for (Coordinate c : spaces) {
                    Tile t = tiles.get(c);
                    if (t instanceof EmptyTile)
                        continue;
                    Graphic g = t.getGraphic();
                    ScreenObject o = new StaticObject(GraphicsConfig.tileToScreen(c), 1, g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Tile_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapObjectArea.this.onMouseDrag(p);
                        }

                        public String toString() {
                            return "MapTile " + c;
                        }
                    };
                    add(o);
                }
                for (Coordinate c : edges) {
                    Path p = paths.get(c);
                    if (p instanceof EmptyPath)
                        continue;
                    Graphic g = null;
                    if (p != null) g = p.getGraphic();
                    if (g == null) g = Team.Blank.paths[c.x % 3];
                    ScreenObject o = new StaticObject(GraphicsConfig.edgeToScreen(c), 2, g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Edge_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapObjectArea.this.onMouseDrag(p);
                        }

                        public String toString() {
                            return "MapEdge " + c;
                        }
                    };
                    add(o);
                }
                for (Coordinate c : vertices) {
                    Building b = buildings.get(c);
                    if (b instanceof EmptyBuilding)
                        continue;
                    Graphic g = null;
                    if (b != null) g = b.getGraphic();
                    if (g == null) g = Team.Blank.settlement[c.x % 2];
                    ScreenObject o = new StaticObject(GraphicsConfig.vertexToScreen(c), 3, g) {

                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Vertex_Clicked, c);
                        }

                        public UserEvent onMouseDrag(Point p) {
                            return MapObjectArea.this.onMouseDrag(p);
                        }

                        public String toString() {
                            return "MapVertex " + c;
                        }
                    };
                    add(o);
                }
            }
        }

        public String toString() {
            return "Backdrop " + game;
        }

    }
}
