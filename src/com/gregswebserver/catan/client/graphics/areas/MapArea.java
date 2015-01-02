package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.renderer.StaticGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.clickables.*;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.util.GraphicsConfig;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 12/31/2014.
 * Class to handle rendering the game board, subclass of ScreenArea with similar functionality.
 */
public class MapArea extends ColorScreenArea {

    private final GameBoard board;
    private final Point backdropScroll;
    private final Backdrop backdrop;

    public MapArea(Dimension size, Point position, int priority, GameBoard board) {
        super(position, priority, new ClickableMap());
        this.size = size;
        this.board = board;
        backdropScroll = new Point();
        ((ClickableMap) clickable).setMapArea(this);
        Dimension gameSize = GraphicsConfig.boardToScreen(board.size);
        backdropScroll.x = -gameSize.width / 2 + size.width / 2;
        backdropScroll.y = -gameSize.height / 2 + size.height / 2;
        backdrop = new Backdrop(backdropScroll, 0, clickable, gameSize);
        limitBackdropScroll();
        add(backdrop);
    }

    @Override
    public Clickable getClickable(Point p) {
        //TODO: figure out where to put this logic.
        Point offsetPosition = new Point(p.x - backdropScroll.x, p.y - backdropScroll.y);
        return backdrop.getClickable(offsetPosition);
    }

    public void scroll(Point p) {
        backdropScroll.translate(p.x, p.y);
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
        if (backdropScroll.x < minX)
            backdropScroll.x = minX;
        if (backdropScroll.y < minY)
            backdropScroll.y = minY;
        if (backdropScroll.x > maxX)
            backdropScroll.x = maxX;
        if (backdropScroll.y > maxY)
            backdropScroll.y = maxY;
        forceRender();
    }

    public void render() {
        //No re-rendering to do here.
    }

    public void update() {
        backdrop.forceRender();
    }

    private class Backdrop extends ColorScreenArea {

        public Backdrop(Point position, int priority, Clickable clickable, Dimension size) {
            super(position, priority, clickable);
            resize(size);
        }

        protected void render() {
//            graphic.displayHitbox(); //TODO: remove
            clear();
            synchronized (board) {
                HashSet<Coordinate> spaces = board.hexArray.spaces.getAllCoordinates();
                HashSet<Coordinate> edges = board.hexArray.edges.getAllCoordinates();
                HashSet<Coordinate> vertices = board.hexArray.vertices.getAllCoordinates();

                HashMap<Coordinate, Tile> tiles = board.hexArray.spaces.toHashMap();
                HashMap<Coordinate, Path> paths = board.hexArray.edges.toHashMap();
                HashMap<Coordinate, Building> buildings = board.hexArray.vertices.toHashMap();

                for (Coordinate c : spaces) {
                    Tile tile = tiles.get(c);
                    Graphic graphic = tile.getGraphic();
                    Clickable clickable = new ClickableTile(c, this.clickable, tile);
                    add(new StaticGraphic(
                            graphic,
                            GraphicsConfig.tileToScreen(c),
                            c.hashCode(),
                            clickable));
                }
                for (Coordinate c : edges) {
                    Path path = paths.get(c);
                    Graphic graphic = null;
                    if (path != null) graphic = path.getGraphic();
                    if (graphic == null) graphic = Team.Blank.paths[c.x % 3];
                    Clickable clickable = new ClickablePath(c, this.clickable, path);
                    add(new StaticGraphic(
                            graphic,
                            GraphicsConfig.edgeToScreen(c),
                            c.hashCode(),
                            clickable));
                }
                for (Coordinate c : vertices) {
                    Building building = buildings.get(c);
                    Graphic graphic = null;
                    if (building != null) graphic = building.getGraphic();
                    if (graphic == null) graphic = Team.Blank.settlement[c.x % 2];
                    Clickable clickable = new ClickableBuilding(c, this.clickable, building);
                    add(new StaticGraphic(
                            graphic,
                            GraphicsConfig.vertexToScreen(c),
                            c.hashCode(),
                            clickable));
                }
            }
        }


    }
}
