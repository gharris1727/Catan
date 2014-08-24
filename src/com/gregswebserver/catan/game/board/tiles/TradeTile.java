package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.board.hexarray.Direction;
import com.gregswebserver.catan.game.gameplay.enums.Resource;
import com.gregswebserver.catan.game.gameplay.enums.TradingPost;
import com.gregswebserver.catan.util.GraphicsConfig;
import com.gregswebserver.catan.util.Statics;

import java.awt.*;
import java.util.HashSet;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private TradingPost tradingPost;
    private HashSet<Direction> directions;
    private Graphic graphic;

    public TradeTile(int sides, Direction direction, TradingPost tradingPost) {
        super(sides, direction);
        this.tradingPost = tradingPost;
        directions = new HashSet<>();
        switch (direction) {
            case up:
                directions.add(Direction.upright);
                directions.add(Direction.upleft);
                break;
            case down:
                directions.add(Direction.downright);
                directions.add(Direction.downleft);
                break;
            case upleft:
                directions.add(Direction.upleft);
                directions.add(Direction.left);
                break;
            case downleft:
                directions.add(Direction.downleft);
                directions.add(Direction.left);
                break;
            case upright:
                directions.add(Direction.upright);
                directions.add(Direction.right);
                break;
            case downright:
                directions.add(Direction.downright);
                directions.add(Direction.right);
                break;
            default:
                //uh.
        }
    }

    public TradingPost getTradingPost() {
        return tradingPost;
    }

    public HashSet<Direction> getTradingPostDirections() {
        return directions;
    }

    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(Statics.tileRenderMask);
            //Copy the underlying graphic (the beach tile) and render on top of it.
            super.getGraphic().renderTo(graphic, null, new Point(), 0);
            for (Direction d : directions) {
                switch (d) {
                    case left:
                        Statics.tradeLeft.renderTo(graphic, null, GraphicsConfig.tradeLeftRender, 0);
                        break;
                    case right:
                        Statics.tradeRight.renderTo(graphic, null, GraphicsConfig.tradeRightRender, 0);
                        break;
                    case upleft:
                        Statics.tradeUpLeft.renderTo(graphic, null, GraphicsConfig.tradeUpLeftRender, 0);
                        break;
                    case downleft:
                        Statics.tradeDownLeft.renderTo(graphic, null, GraphicsConfig.tradeDownLeftRender, 0);
                        break;
                    case upright:
                        Statics.tradeUpRight.renderTo(graphic, null, GraphicsConfig.tradeUpRightRender, 0);
                        break;
                    case downright:
                        Statics.tradeDownRight.renderTo(graphic, null, GraphicsConfig.tradeDownRightRender, 0);
                        break;
                }
            }
            Graphic icon = Statics.questionIconTexture;
            Graphic ratio = Statics.tradeRatioThree;
            Resource res = tradingPost.getResource();
            if (res != null) {
                icon = res.getIcon();
                ratio = Statics.tradeRatioTwo;
            }
            icon.renderTo(graphic, null, GraphicsConfig.tradeTileResourceIconRender, 0);
            ratio.renderTo(graphic, null, GraphicsConfig.tradeTileRatioRender, 0);
        }
        return graphic;
    }

    public String toString() {
        return "TradeTile " + tradingPost;
    }
}
