package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.*;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 2/7/16.
 * A rendering area for the game board.
 */
public class MapRegion extends ScrollingScreenRegion {

    //TODO: move these to config files.
    private final Point[] bridgePositions = new Point[]{
            new Point(), new Point(), new Point(), // C U D
            new Point(0,42), new Point(70,42), // L R
            new Point(20,-3), new Point(20,65),  // UL DL
            new Point(60,-3), new Point(55,65) // UR DR
    };

    private GraphicSet resources;
    private GraphicSet diceRolls;
    private GraphicSet singleBeach;
    private GraphicSet doubleBeach;
    private GraphicSet resourceIcons;
    private GraphicSet tradeBridges;
    private Map<Team,GraphicSet> buildings;

    private final GameBoard board;
    private final TeamColors teamColors;

    private final TiledBackground background;

    public MapRegion(GameBoard board, TeamColors teamColors) {
        super(0, "map");
        //Store the instance information.
        this.board = board;
        this.teamColors = teamColors;
        //Create the sub-regions
        background = new EdgedTiledBackground(0, "background");
        //Add everything to the screen.
        add(background).setClickable(this);
    }

    public GraphicSet getBuildingGraphics(Team team) {
        return buildings.get(team);
    }

    public GraphicSet getResourceGraphics() {
        return resources;
    }

    public GraphicSet getSingleBeachGraphics() {
        return singleBeach;
    }

    public GraphicSet getDoubleBeachGraphics() {
        return doubleBeach;
    }

    public GraphicSet getBridgeGraphics() {
        return tradeBridges;
    }

    public Point[] getBridgePositions() {
        return bridgePositions;
    }

    public GraphicSet getDiceRollGraphics() {
        return diceRolls;
    }

    public GraphicSet getTradeIcons() {
        return resourceIcons;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        resources = new GraphicSet(config.getLayout(), "land", HexagonalMask.class, null);
        diceRolls = new GraphicSet(config.getLayout(), "dice", RoundedMask.class, null);
        singleBeach = new GraphicSet(config.getLayout(), "singlebeach", HexagonalMask.class, null);
        doubleBeach = new GraphicSet(config.getLayout(), "doublebeach", HexagonalMask.class, null);
        resourceIcons = new GraphicSet(config.getLayout(), "trade", RoundedMask.class, null);

        GraphicSourceInfo bridgeSource = new GraphicSourceInfo(config.getLayout().get("bridges.path"));
        RenderMask bridgeHorizontal = new RectangularMask(config.getLayout().getDimension("bridges.horizontal.size"));
        RenderMask bridgeDiagonalUp = new AngleRectangularMask(config.getLayout().getDimension("bridges.diagonal.size"));
        RenderMask brigeDiagonalDown = new FlippedMask(bridgeDiagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask[] masks = new RenderMask[]{null, null, null, bridgeHorizontal, bridgeHorizontal, brigeDiagonalDown, bridgeDiagonalUp, bridgeDiagonalUp, brigeDiagonalDown};
        tradeBridges = new GraphicSet(bridgeSource, masks, null);

        GraphicSourceInfo buildingSource = new GraphicSourceInfo(config.getLayout().get("buildings.source"));
        RenderMask buildingHorizontal = new RectangularMask(config.getLayout().getDimension("buildings.horizontal.size"));
        RenderMask buildingDiagonalUp = new AngleRectangularMask(config.getLayout().getDimension("buildings.diagonal.size"),
                config.getLayout().getDimension("buildings.diagonal.offset"));
        RenderMask buildingDiagonalDown = new FlippedMask(buildingDiagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask settlement = new RoundedMask(config.getLayout().getDimension("buildings.settlement.size"));
        RenderMask city =new RoundedMask(config.getLayout().getDimension("buildings.city.size"));
        RenderMask robber = new RectangularMask(config.getLayout().getDimension("buildings.robber.size"));
        RenderMask[] buildingMasks = new RenderMask[]{buildingHorizontal, buildingDiagonalUp, buildingDiagonalDown, settlement, city, robber};
        buildings = new EnumMap<>(Team.class);
        for (Team team : Team.values())
            buildings.put(team,new GraphicSet(buildingSource, buildingMasks, teamColors.getSwaps(team)));
        setMask(new RectangularMask(boardToScreen(board.getSize())));
    }

    @Override
    protected void renderContents() {
        clear();
        for (Tile tile : board.getTileMap().values())
            add(new TileObject(this, tile)).setPosition(tileToScreen(tile.getPosition()));
        for (Path path : board.getPathMap().values())
            add(new PathObject(this, path)).setPosition(edgeToScreen(path.getPosition()));
        for (Town town : board.getTownMap().values())
            add(new TownObject(this, town)).setPosition(vertexToScreen(town.getPosition()));
        add(background);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        scroll(p.x, p.y);
        return null;
    }

    public String toString() {
        return "MapRegion";
    }

    private static final int[][] tileOffsets = {
            {12, 112}, //Horizontal
            {16, 72}}; //Vertical
    private static final int[][] edgeOffsets = {
            {0, 0, 36, 100, 100, 136}, //Horizontal
            {9, 65, 0, 9, 65, 56}}; //Vertical
    private static final int[][] vertOffsets = {
            {0, 24, 100, 124}, //Horizontal
            {56, 0, 0, 56}}; //Vertical

    private Dimension boardToScreen(Dimension size) {
        Dimension unitSize = getConfig().getLayout().getDimension("unit");
        int outW = ((size.width + 1) / 2) * unitSize.width;
        int outH = (size.height + 1) * unitSize.height;
        return new Dimension(outW, outH);
    }

    private Point tileToScreen(Coordinate c) {
        Dimension unitSize = getConfig().getLayout().getDimension("unit");
        int outX = (c.x / 2) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    private Point edgeToScreen(Coordinate c) {
        Dimension unitSize = getConfig().getLayout().getDimension("unit");
        int outX = (c.x / 6) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    private Point vertexToScreen(Coordinate c) {
        Dimension unitSize = getConfig().getLayout().getDimension("unit");
        int outX = (c.x / 4) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += vertOffsets[0][c.x % 4]-4;
        outY += vertOffsets[1][c.x % 4]-2;
        return new Point(outX, outY);
    }
}