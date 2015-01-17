package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;

import static com.gregswebserver.catan.common.game.gameplay.enums.GameResource.*;

/**
 * Created by Greg on 8/13/2014.
 * Enum of purchase-able items for use in the trading framework.
 */
public enum Purchase {

    Road(new Tradeable[]{Brick, Lumber}),
    Settlement(new Tradeable[]{Brick, Lumber, Grain, Wool}),
    City(new Tradeable[]{Grain, Grain, Ore, Ore, Ore}),
    DevelopmentCard(new Tradeable[]{Wool, Grain, Ore});

    private Tradeable[] cost;

    Purchase(Tradeable[] cost) {
        this.cost = cost;
    }
}
