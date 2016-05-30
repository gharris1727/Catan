package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.FlippedMask;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.trade.TradingPostType;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.util.Direction;

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
    private final GameBoard board;

    //Optional interaction: context menus for contextual information.
    private ContextRegion context;

    //Configuration dependencies
    private Point[] bridgePositions;
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

    public MapRegion(GameBoard board) {
        super(0, "map");
        //Store the instance information.
        this.board = board;
        //Create the sub-regions
        background = new EdgedTiledBackground(0, "background");
        //Add everything to the screen.
        add(background).setClickable(this);
    }

    public void setContext(ContextRegion context) {
        this.context = context;
    }

    @Override
    public void loadConfig(UIConfig config) {
        unitSize = config.getLayout().getDimension("unit");
        resources = new GraphicSet(config.getLayout(), "land", null);
        diceRolls = new GraphicSet(config.getLayout(), "dice", null);
        singleBeach = new GraphicSet(config.getLayout(), "singlebeach", null);
        doubleBeach = new GraphicSet(config.getLayout(), "doublebeach", null);
        resourceIcons = new GraphicSet(config.getLayout(), "trade", null);

        Point bridgeleft = config.getLayout().getPoint("bridges.left");
        Point bridgeRight = config.getLayout().getPoint("bridges.right");
        Point bridgeUpLeft = config.getLayout().getPoint("bridges.upleft");
        Point bridgeDownLeft = config.getLayout().getPoint("bridges.downleft");
        Point bridgeUpRight = config.getLayout().getPoint("bridges.upright");
        Point bridgeDownRight = config.getLayout().getPoint("bridges.downright");
        bridgePositions = new Point[] {null, null, null, bridgeleft, bridgeRight, bridgeUpLeft, bridgeDownLeft, bridgeUpRight, bridgeDownRight};

        GraphicSourceInfo bridgeSource = new GraphicSourceInfo(config.getLayout().get("bridges.path"));
        RenderMask bridgeHorizontal = RenderMask.parseMask(config.getLayout().narrow("bridges.horizontal"));
        RenderMask bridgeDiagonalUp = RenderMask.parseMask(config.getLayout().narrow("bridges.diagonal.up"));
        RenderMask brigeDiagonalDown = new FlippedMask(bridgeDiagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask[] masks = new RenderMask[]{null, null, null, bridgeHorizontal, bridgeHorizontal, brigeDiagonalDown, bridgeDiagonalUp, bridgeDiagonalUp, brigeDiagonalDown};
        tradeBridges = new GraphicSet(bridgeSource, masks, null);

        GraphicSourceInfo buildingSource = new GraphicSourceInfo(config.getLayout().get("buildings.source"));
        RenderMask buildingHorizontal = RenderMask.parseMask(config.getLayout().narrow("buildings.horizontal"));
        RenderMask buildingDiagonalUp = RenderMask.parseMask(config.getLayout().narrow("buildings.diagonal.up"));
        RenderMask buildingDiagonalDown = RenderMask.parseMask(config.getLayout().narrow("buildings.diagonal.down"));
        RenderMask settlement = RenderMask.parseMask(config.getLayout().narrow("buildings.settlement"));
        RenderMask city = RenderMask.parseMask(config.getLayout().narrow("buildings.city"));
        RenderMask robber = RenderMask.parseMask(config.getLayout().narrow("buildings.robber"));
        RenderMask[] buildingMasks = new RenderMask[]{buildingHorizontal, buildingDiagonalUp, buildingDiagonalDown, settlement, city, robber};
        buildings = new EnumMap<>(TeamColor.class);
        TeamColorSwaps teamColorSwaps = new TeamColorSwaps(config.getTeamColors());
        for (TeamColor teamColor : TeamColor.values())
            buildings.put(teamColor, new GraphicSet(buildingSource, buildingMasks, teamColorSwaps.getSwaps(teamColor)));
        RectangularMask mask = new RectangularMask(boardToScreen(board.getSize()));

        Dimension borderBuffer = config.getLayout().getDimension("borderbuffer");
        setInsets(new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width));
        setMask(mask);
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        clear();
        for (Tile tile : board.getTileMap().values())
            add(new TileObject(tile)).setPosition(tileToScreen(tile.getPosition()));
        for (Path path : board.getPathMap().values())
            add(new PathObject(path)).setPosition(edgeToScreen(path.getPosition()));
        for (Town town : board.getTownMap().values())
            add(new TownObject(town)).setPosition(vertexToScreen(town.getPosition()));
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

    //TODO: load these from config data.
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
        int outW = ((size.width + 1) / 2) * unitSize.width;
        int outH = (size.height + 1) * unitSize.height;
        return new Dimension(outW, outH);
    }

    private Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    private Point edgeToScreen(Coordinate c) {
        int outX = (c.x / 6) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    private Point vertexToScreen(Coordinate c) {
        int outX = (c.x / 4) * unitSize.width;
        int outY = (c.y) * unitSize.height;
        outX += vertOffsets[0][c.x % 4]-4;
        outY += vertOffsets[1][c.x % 4]-2;
        return new Point(outX, outY);
    }

    private abstract class MapObject extends ScreenRegion {

        private MapObject(int priority) {
            super(priority);
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            return MapRegion.this.onMouseDrag(p);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }
    }

    private class PathObject extends MapObject {

        private final Path path;

        private PathObject(Path path) {
            super(1);
            this.path = path;
            int orientation = 0;
            if (path.getPosition().x % 6 == 0 || path.getPosition().x % 6 == 4) orientation = 1;
            if (path.getPosition().x % 6 == 1 || path.getPosition().x % 6 == 3) orientation = 2;
            Graphic background = buildings.get(path.getTeam()).getGraphic(orientation);
            add(new GraphicObject(0, background) {
                @Override
                public String toString() {
                    return "PathBackground";
                }
            }).setClickable(this);
            setMask(background.getMask());
        }

        @Override
        public UserEvent onMouseClick(MouseEvent event) {
            if (context != null)
                context.targetPath(path.getPosition());
            return null;
        }

        @Override
        public String toString() {
            return "PathObject";
        }
    }

    private class TileObject extends MapObject {

        private final Tile tile;

        private TileObject(Tile tile) {
            super(1);
            //Load layout information
            this.tile = tile;
            setMask(resources.getMask());
            Graphic background;
            if (tile instanceof ResourceTile) {
                ResourceTile resource = (ResourceTile) tile;
                background = resources.getGraphic(resource.getTerrain().ordinal());
                center(add(new DiceRollGraphicObject(resource)));
                if (resource.hasRobber())
                    center(add(new RobberGraphicObject(buildings.get(TeamColor.None).getGraphic(5))));
            } else if (tile instanceof BeachTile) {
                BeachTile beach = (BeachTile) tile;
                background = ( beach.getSides() == 1 ? singleBeach : doubleBeach).getGraphic(beach.getDirection().ordinal());
                if (tile instanceof TradeTile) {
                    TradeTile trade = (TradeTile) tile;
                    for (Direction d : trade.getTradingPostDirections())
                        add(new TradeBridgeGraphicObject(d));
                    center(add(new TradeIconGraphicObject(trade.getTradingPostType())));
                }
            } else
                throw new IllegalStateException();
            add(new TileBackgroundGraphicObject(background));
        }

        @Override
        public UserEvent onMouseClick(MouseEvent event) {
            if (context != null)
                context.targetTile(tile.getPosition());
            return null;
        }

        @Override
        public String toString() {
            return "TileObject";
        }

        private class TradeBridgeGraphicObject extends GraphicObject {

            private TradeBridgeGraphicObject(Direction d) {
                super(1, tradeBridges.getGraphic(d.ordinal()));
                setPosition(bridgePositions[d.ordinal()]);
                setClickable(TileObject.this);
            }

            @Override
            public String toString() {
                return "TradeBridge";
            }
        }

        private class DiceRollGraphicObject extends GraphicObject {

            private DiceRollGraphicObject(ResourceTile resource) {
                super(1, diceRolls.getGraphic(resource.getDiceRoll().ordinal()));
                setClickable(TileObject.this);
            }

            @Override
            public String toString() {
                return "TileDiceRoll";
            }
        }

        private class TileBackgroundGraphicObject extends GraphicObject {

            private TileBackgroundGraphicObject(Graphic graphic) {
                super(0, graphic);
                setClickable(TileObject.this);
            }

            @Override
            public String toString() {
                return "TileBackgroundGraphicObject";
            }
        }

        private class TradeIconGraphicObject extends GraphicObject {
            private TradeIconGraphicObject(TradingPostType tradingPostType) {
                super(2, resourceIcons.getGraphic(tradingPostType.ordinal()));
                setClickable(TileObject.this);
            }

            @Override
            public String toString() {
                return "TradeIconGraphicObject";
            }
        }

        private class RobberGraphicObject extends GraphicObject {
            private RobberGraphicObject(Graphic robberGraphic) {
                super(2, robberGraphic);
            }

            @Override
            public String toString() {
                return "TileRobberGraphicObject";
            }
        }
    }

    private class TownObject extends MapObject {

        private final Town town;

        private TownObject(Town town) {
            super(2);
            this.town = town;
            Graphic background;
            if (town instanceof Settlement || town instanceof EmptyTown) {
                background = buildings.get(town.getTeam()).getGraphic(3);
            } else if (town instanceof City) {
                background = buildings.get(town.getTeam()).getGraphic(4);
            } else
                throw new IllegalStateException();
            add(new GraphicObject(0, background) {
                @Override
                public String toString() {
                    return "TownObjectBackground";
                }
            }).setClickable(this);
            setMask(background.getMask());
        }

        @Override
        public UserEvent onMouseClick(MouseEvent event) {
            if (context != null)
                context.targetTown(town.getPosition());
            return null;
        }

        @Override
        public String toString() {
            return "TownObject";
        }
    }
}
