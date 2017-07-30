package catan.client.ui.game;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.masks.FlippedMask;
import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.client.graphics.screen.ScreenRegion;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.ScrollingScreenRegion;
import catan.client.graphics.ui.TiledBackground;
import catan.client.graphics.ui.UIConfig;
import catan.client.input.UserEventListener;
import catan.common.IllegalStateException;
import catan.common.config.ConfigSource;
import catan.common.game.BoardObserver;
import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.paths.Path;
import catan.common.game.board.tiles.BeachTile;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.tiles.TradeTile;
import catan.common.game.board.towns.City;
import catan.common.game.board.towns.EmptyTown;
import catan.common.game.board.towns.Settlement;
import catan.common.game.board.towns.Town;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;
import catan.common.game.teams.TeamColor;
import catan.common.resources.GraphicSet;
import catan.common.resources.GraphicSourceInfo;
import catan.common.util.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 2/7/16.
 * Screen region primarily responsible for displaying the current state of a GameBoard object.
 * Contains configuration and graphics data that is shared with the child MapObjects.
 * Optionally can have a connected context region where user interaction information is sent.
 */
public class MapRegion extends ScrollingScreenRegion {

    //Required instance information.
    private final BoardObserver board;

    //Optional interaction: context menus for contextual information.
    private ContextRegion context;

    //Configuration dependencies
    private Point[] bridgePositions;
    private Point[] tilePositions;
    private Point[] pathPositions;
    private Point[] townPositions;
    private Point townOffset;
    private GraphicSet resources;
    private GraphicSet diceRolls;
    private GraphicSet singleBeach;
    private GraphicSet doubleBeach;
    private GraphicSet resourceIcons;
    private GraphicSet tradeBridges;
    private Map<TeamColor,GraphicSet> buildings;
    private Dimension unitSize;

    //Sub-regions
    private final TiledBackground background;

    public MapRegion(BoardObserver board) {
        super("MapRegion", 0, "map");
        //Store the instance information.
        this.board = board;
        //Create the sub-regions
        background = new EdgedTiledBackground();
        //Add everything to the screen.
        add(background).setClickable(this);
    }

    public void setContext(ContextRegion context) {
        this.context = context;
    }

    @Override
    public void loadConfig(UIConfig config) {
        ConfigSource layout = config.getLayout();
        unitSize = layout.getDimension("unit");
        resources = new GraphicSet(layout, "land", null);
        diceRolls = new GraphicSet(layout, "dice", null);
        singleBeach = new GraphicSet(layout, "singlebeach", null);
        doubleBeach = new GraphicSet(layout, "doublebeach", null);
        resourceIcons = new GraphicSet(layout, "trade", null);

        Point bridgeleft = layout.getPoint("bridges.left");
        Point bridgeRight = layout.getPoint("bridges.right");
        Point bridgeUpLeft = layout.getPoint("bridges.upleft");
        Point bridgeDownLeft = layout.getPoint("bridges.downleft");
        Point bridgeUpRight = layout.getPoint("bridges.upright");
        Point bridgeDownRight = layout.getPoint("bridges.downright");
        bridgePositions = new Point[] {null, null, null, bridgeleft, bridgeRight, bridgeUpLeft, bridgeDownLeft, bridgeUpRight, bridgeDownRight};

        Point tile0 = layout.getPoint("tiles.0");
        Point tile1 = layout.getPoint("tiles.1");
        tilePositions = new Point[] {tile0, tile1};

        Point path0 = layout.getPoint("paths.0");
        Point path1 = layout.getPoint("paths.1");
        Point path2 = layout.getPoint("paths.2");
        Point path3 = layout.getPoint("paths.3");
        Point path4 = layout.getPoint("paths.4");
        Point path5 = layout.getPoint("paths.5");
        pathPositions = new Point[] {path0, path1, path2, path3, path4, path5};

        Point town0 = layout.getPoint("towns.0");
        Point town1 = layout.getPoint("towns.1");
        Point town2 = layout.getPoint("towns.2");
        Point town3 = layout.getPoint("towns.3");
        townPositions = new Point[] {town0, town1, town2, town3};

        townOffset = layout.getPoint("towns.offset");

        GraphicSourceInfo bridgeSource = new GraphicSourceInfo(layout.get("bridges.path"));
        RenderMask bridgeHorizontal = layout.getRenderMask("bridges.horizontal");
        RenderMask bridgeDiagonalUp = layout.getRenderMask("bridges.diagonal.up");
        RenderMask brigeDiagonalDown = new FlippedMask(bridgeDiagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask[] masks = {null, null, null, bridgeHorizontal, bridgeHorizontal, brigeDiagonalDown, bridgeDiagonalUp, bridgeDiagonalUp, brigeDiagonalDown};
        tradeBridges = new GraphicSet(bridgeSource, masks, null);

        GraphicSourceInfo buildingSource = new GraphicSourceInfo(layout.get("buildings.source"));
        RenderMask buildingHorizontal = layout.getRenderMask("buildings.horizontal");
        RenderMask buildingDiagonalUp = layout.getRenderMask("buildings.diagonal.up");
        RenderMask buildingDiagonalDown = layout.getRenderMask("buildings.diagonal.down");
        RenderMask settlement = layout.getRenderMask("buildings.settlement");
        RenderMask city = layout.getRenderMask("buildings.city");
        RenderMask robber = layout.getRenderMask("buildings.robber");
        RenderMask[] buildingMasks = {buildingHorizontal, buildingDiagonalUp, buildingDiagonalDown, settlement, city, robber};
        buildings = new EnumMap<>(TeamColor.class);
        TeamColorSwaps teamColorSwaps = new TeamColorSwaps(config.getTeamColors());
        for (TeamColor teamColor : TeamColor.values())
            buildings.put(teamColor, new GraphicSet(buildingSource, buildingMasks, teamColorSwaps.getSwaps(teamColor)));

        Dimension borderBuffer = layout.getDimension("borderbuffer");
        setInsets(new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width));

        RectangularMask mask = new RectangularMask(boardToScreen(board.getSize()));
        setMask(mask);
        background.setMask(mask);
    }

    @Override
    public void update() {
        forceRender();
    }

    @Override
    protected void renderContents() {
        clear();
        board.eachTile(tile -> add(new TileObject(tile)).setPosition(tileToScreen(tile.getPosition())));
        board.eachPath(path -> add(new PathObject(path)).setPosition(pathToScreen(path.getPosition())));
        board.eachTown(town -> add(new TownObject(town)).setPosition(townToScreen(town.getPosition())));
        add(background);
    }

    @Override
    public void onMouseDrag(UserEventListener listener, Point p) {
        scroll(p.x, p.y);
    }

    private Dimension boardToScreen(Dimension size) {
        int outW = ((size.width + 1) / 2) * unitSize.width;
        int outH = (size.height + 1) * unitSize.height;
        return new Dimension(outW, outH);
    }

    private Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += tilePositions[c.x % 2].x;
        outY += tilePositions[c.x % 2].y;
        return new Point(outX, outY);
    }

    private Point pathToScreen(Coordinate c) {
        int outX = (c.x / 6) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += pathPositions[c.x % 6].x;
        outY += pathPositions[c.x % 6].y;
        return new Point(outX, outY);
    }

    private Point townToScreen(Coordinate c) {
        int outX = (c.x / 4) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += townOffset.x;
        outY += townOffset.y;
        outX += townPositions[c.x % 4].x;
        outY += townPositions[c.x % 4].y;
        return new Point(outX, outY);
    }

    private abstract class MapObject extends ScreenRegion {

        private MapObject(String name, int priority) {
            super(name, priority);
        }

        @Override
        public void onMouseDrag(UserEventListener listener, Point p) {
            MapRegion.this.onMouseDrag(listener, p);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }
    }

    private class PathObject extends MapObject {

        private final Path path;

        private PathObject(Path path) {
            super(path.toString(), 1);
            this.path = path;
            int orientation = CoordTransforms.getEdgeOrientation(path.getPosition());
            //Load the background graphic based on the team and the orientation chosen.
            Graphic background = buildings.get(path.getTeam()).getGraphic(orientation);
            //Add a new graphic object using that graphic background.
            add(new GraphicObject("PathBackground", 0, background)).setClickable(this);
            //Shape this object based on the background.
            setMask(background.getMask());
        }

        @Override
        public void onMouseClick(UserEventListener listener, MouseEvent event) {
            if (context != null)
                context.targetPath(path.getPosition());
        }
    }

    private class TileObject extends MapObject {

        private final Tile tile;

        private TileObject(Tile tile) {
            super(tile.toString(), 1);
            this.tile = tile;
            //We know our size ahead of time, its always a hexagon.
            setMask(resources.getMask());
            //We need to choose a background image based on the contents of the tile.
            Graphic background;
            //Break into cases of the different types of tiles.
            if (tile instanceof ResourceTile) {
                ResourceTile resource = (ResourceTile) tile;
                //The background of a resource tile come from the resources GraphicSet.
                background = resources.getGraphic(resource.getTerrain().ordinal());
                //Get the dice roll from the resource tile.
                DiceRoll diceRoll = resource.getDiceRoll();
                //Create the graphic for the dice roll chit.
                GraphicObject diceRollGraphic = new GraphicObject(diceRoll.toString(), 1,  diceRolls.getGraphic(diceRoll.ordinal()));
                //Add the dice roll graphic to the object.
                add(diceRollGraphic).setClickable(this);
                //Center it on the tile.
                center(diceRollGraphic);
                //If we are robbed, we need to render the robber.
                if (resource.hasRobber()) {
                    //Create the graphic for the robber.
                    GraphicObject robberGraphic = new GraphicObject("RobberGraphic", 2, buildings.get(TeamColor.None).getGraphic(5));
                    //Add it to the screen
                    add(robberGraphic).setClickable(this);
                    //Center it on the tile.
                    center(robberGraphic);
                }
            } else if (tile instanceof BeachTile) {
                BeachTile beach = (BeachTile) tile;
                //The beach tile background depends on the number of sides and the direction of the beach.
                background = ((beach.getSides() == 1) ? singleBeach : doubleBeach).getGraphic(beach.getDirection().ordinal());
                //If it also happens to be a trading post, then we need to render the trading post details.
                if (tile instanceof TradeTile) {
                    TradeTile trade = (TradeTile) tile;
                    //For each of the trading post directions, we need to render a bridge.
                    for (Direction d : trade.getTradingPostDirections()) {
                        //Create the bridge graphic
                        GraphicObject bridgeGraphic = new GraphicObject(d + " Bridge", 1, tradeBridges.getGraphic(d.ordinal()));
                        //Place it based on the preset position data.
                        add(bridgeGraphic).setClickable(this).setPosition(bridgePositions[d.ordinal()]);
                    }
                    //Get the type of trading post we need to render.
                    TradingPostType tradingPostType = trade.getTradingPostType();
                    //Create the trade icon graphic
                    GraphicObject tradeIcon = new GraphicObject(tradingPostType.toString(), 2, resourceIcons.getGraphic(tradingPostType.ordinal()));
                    //Add the trade icon to the tile
                    add(tradeIcon).setClickable(this);
                    //Center it on the tile
                    center(tradeIcon);
                }
            } else //We dont know how to render this tile.
                throw new IllegalStateException();
            //Add the background object itself to the screen.
            add(new GraphicObject("TileBackground", 0, background)).setClickable(this);
        }

        @Override
        public void onMouseClick(UserEventListener listener, MouseEvent event) {
            if (context != null)
                context.targetTile(tile.getPosition());
        }

    }

    private class TownObject extends MapObject {

        private final Town town;

        private TownObject(Town town) {
            super(town.toString(), 2);
            this.town = town;
            //Decide on the background graphic
            Graphic background;
            if ((town instanceof Settlement) || (town instanceof EmptyTown)) {
                //We render it as a settlement
                background = buildings.get(town.getTeam()).getGraphic(3);
            } else if (town instanceof City) {
                //We render it as a city.
                background = buildings.get(town.getTeam()).getGraphic(4);
            } else //We dont know how to render it.
                throw new IllegalStateException();
            //Add the background to the screen.
            add(new GraphicObject("TownBackground", 0, background)).setClickable(this);
            //Shape this like the town graphic.
            setMask(background.getMask());
        }

        @Override
        public void onMouseClick(UserEventListener listener, MouseEvent event) {
            if (context != null)
                context.targetTown(town.getPosition());
        }
    }
}
