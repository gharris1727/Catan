package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.input.clickables.ClickableBuilding;
import com.gregswebserver.catan.client.input.clickables.ClickablePath;
import com.gregswebserver.catan.client.input.clickables.ClickableTile;
import com.gregswebserver.catan.common.game.CatanGame;
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
public class GameArea extends ScreenArea {

    private CatanGame game;
    private Point mapScroll;
    private ScreenArea map;

    public GameArea(Dimension size, Point position, int priority) {
        super(size, position, priority);
        mapScroll = new Point();
    }

    public void scroll(Point p) {
        mapScroll.translate(p.x, p.y);
        limitMapScroll();
        forceRender();
    }

    public void resize(Dimension d) {
        super.resize(d);
        limitMapScroll();
    }

    private void limitMapScroll() {
        if (map != null) {
            int maxX = -GraphicsConfig.mapEdgeBufferSize.width;
            int maxY = -GraphicsConfig.mapEdgeBufferSize.height;
            int minX = -map.getSize().width + size.width - 2 * maxX;
            int minY = -map.getSize().height + size.height - 2 * maxY;
            if (mapScroll.x < minX)
                mapScroll.x = minX;
            if (mapScroll.y < minY)
                mapScroll.y = minY;
            if (mapScroll.x > maxX)
                mapScroll.x = maxX;
            if (mapScroll.y > maxY)
                mapScroll.y = maxY;
        }
    }

    public void setGame(CatanGame game) {
        this.game = game;
        Dimension gameSize = GraphicsConfig.boardToScreen(game.getBoard().size);
        //TODO: ensure that the map scrolls to the center when game starts.
        mapScroll.x = -gameSize.width / 2 + size.width / 2;
        mapScroll.y = -gameSize.height / 2 + size.height / 2;
        map = new ScreenArea(gameSize, mapScroll, 0) {
            protected void render() {
                preRender();
                if (GameArea.this.game.getBoard() != null) {
                    HashSet<Coordinate> spaces = game.getBoard().hexArray.spaces.getAllCoordinates();
                    HashSet<Coordinate> edges = game.getBoard().hexArray.edges.getAllCoordinates();
                    HashSet<Coordinate> vertices = game.getBoard().hexArray.vertices.getAllCoordinates();

                    HashMap<Coordinate, Tile> tiles = game.getBoard().hexArray.spaces.toHashMap();
                    HashMap<Coordinate, Path> paths = game.getBoard().hexArray.edges.toHashMap();
                    HashMap<Coordinate, Building> buildings = game.getBoard().hexArray.vertices.toHashMap();

                    for (Coordinate c : spaces) {
                        Tile tile = tiles.get(c);
                        Graphic graphic = tile.getGraphic();
                        Clickable clickable = new ClickableTile(c, tile);
                        addScreenObject(new StaticGraphic(
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
                        Clickable clickable = new ClickablePath(c, path);
                        addScreenObject(new StaticGraphic(
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
                        Clickable clickable = new ClickableBuilding(c, building);
                        addScreenObject(new StaticGraphic(
                                graphic,
                                GraphicsConfig.vertexToScreen(c),
                                c.hashCode(),
                                clickable));
                    }
                }
                postRender();
            }
        };
        limitMapScroll();
        addScreenObject(map);
    }

    public void update() {
        map.forceRender();
    }
}
