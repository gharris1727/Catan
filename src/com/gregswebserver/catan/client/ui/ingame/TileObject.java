package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 2/7/16.
 * Class that contains the graphical representation of a Tile object.
 */
public class TileObject extends MapObject {

    private final MapRegion container;
    private final Tile tile;

    TileObject(MapRegion container, Tile tile) {
        super(1, container);
        //Load layout information
        this.container = container;
        this.tile = tile;
        setMask(container.getResourceGraphics().getMask());
        Graphic background;
        if (tile instanceof ResourceTile) {
            ResourceTile resource = (ResourceTile) tile;
            background = container.getResourceGraphics().getGraphic(resource.getTerrain().ordinal());
            center(add(new DiceRollGraphicObject(resource)));
            if (resource.hasRobber())
                center(add(new RobberGraphicObject(container.getBuildingGraphics(TeamColor.None).getGraphic(5))));
        } else if (tile instanceof BeachTile) {
            BeachTile beach = (BeachTile) tile;
            background = ( beach.getSides() == 1 ? container.getSingleBeachGraphics() : container.getDoubleBeachGraphics()).getGraphic(beach.getDirection().ordinal());
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
        container.target(tile);
        return null;
    }

    @Override
    public String toString() {
        return "TileObject";
    }

    private class TradeBridgeGraphicObject extends GraphicObject {

        private TradeBridgeGraphicObject(Direction d) {
            super(1, getContainer().getBridgeGraphics().getGraphic(d.ordinal()));
            setPosition(getContainer().getBridgePositions()[d.ordinal()]);
            setClickable(TileObject.this);
        }

        @Override
        public String toString() {
            return "TradeBridge";
        }
    }

    private class DiceRollGraphicObject extends GraphicObject {

        private DiceRollGraphicObject(ResourceTile resource) {
            super(1, getContainer().getDiceRollGraphics().getGraphic(resource.getDiceRoll().ordinal()));
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
            super(2, getContainer().getTradeIcons().getGraphic(tradingPostType.ordinal()));
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
