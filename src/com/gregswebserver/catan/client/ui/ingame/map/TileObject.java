package com.gregswebserver.catan.client.ui.ingame.map;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.*;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 2/7/16.
 * Class that contains the graphical representation of a Tile object.
 */
public class TileObject extends ScreenRegion {

    private static final RenderMask tileMask = new HexagonalMask(Client.staticConfig.getDimension("catan.graphics.tiles.size"));
    private static final Point diceRollOffet = Client.staticConfig.getPoint("catan.graphics.game.dice.offset");

    private static final Point[] bridgePositions = new Point[]{
            new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point()
    };

    private static final GraphicSet resources;
    private static final GraphicSet diceRolls;

    private static final GraphicSet singleBeach;
    private static final GraphicSet doubleBeach;

    private static final GraphicSet resourceIcons;
    private static final GraphicSet tradeBridges;

    static {
        resources = new GraphicSet("catan.graphics.game.resource", RoundedRectangularMask.class);
        diceRolls = new GraphicSet("catan.graphics.game.dice", RoundedMask.class);

        singleBeach = new GraphicSet("catan.graphics.tiles.singlebeach", HexagonalMask.class);
        doubleBeach = new GraphicSet("catan.graphics.tiles.doublebeach", HexagonalMask.class);

        resourceIcons = new GraphicSet("catan.graphics.trade.icons", RectangularMask.class);
        GraphicSourceInfo source = new GraphicSourceInfo(Client.staticConfig.get("catan.graphics.trade.bridge.path"));
        RenderMask horizontal = new RectangularMask(Client.staticConfig.getDimension("catan.graphics.trade.bridge.horizontal.size"));
        RenderMask diagonalUp = new DiagonalMask(Client.staticConfig.getDimension("catan.graphics.trade.bridge.diagonal.size"));
        RenderMask diagonalDown = new FlippedMask(diagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask[] masks = new RenderMask[]{null, null, null, horizontal,
                horizontal, diagonalDown, diagonalDown, diagonalUp, diagonalUp};
        tradeBridges = new GraphicSet(source,masks);
    }

    private final Tile tile;

    TileObject(int priority, Tile tile) {
        super(priority);
        this.tile = tile;
        Graphic background;
        if (tile instanceof ResourceTile) {
            ResourceTile resource = (ResourceTile) tile;
            background = resources.getGraphic(resource.getTerrain().ordinal());
            add(new DiceRollGraphicObject(resource));
            //TODO: render the robber
        } else if (tile instanceof BeachTile) {
            BeachTile beach = (BeachTile) tile;
            background = ( beach.getSides() == 1 ? singleBeach : doubleBeach).getGraphic(beach.getDirection().ordinal());
            if (tile instanceof TradeTile) {
                TradeTile trade = (TradeTile) tile;
                for (Direction d : trade.getTradingPostDirections())
                    add(new TradeBridgeGraphicObject(d));
            }
        } else
            throw new IllegalStateException();
        add(new TileBackgroundGraphicObject(background));
        setMask(tileMask);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return new UserEvent(this, UserEventType.Tile_Clicked, tile.getPosition());
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
            setPosition(diceRollOffet);
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
}
