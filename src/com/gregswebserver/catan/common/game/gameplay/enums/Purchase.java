package com.gregswebserver.catan.common.game.gameplay.enums;

import static com.gregswebserver.catan.common.game.gameplay.enums.GameResource.*;

/**
 * Created by Greg on 8/13/2014.
 * Enum of purchase-able items for use in the trading framework.
 */
public enum Purchase {

    Road(new GameResource[]{Brick, Lumber}),
    Settlement(new GameResource[]{Brick, Lumber, Grain, Wool}),
    City(new GameResource[]{Grain, Grain, Ore, Ore, Ore}),
    DevelopmentCard(new GameResource[]{Wool, Grain, Ore});

    public final GameResource[] cost;

    Purchase(GameResource[] cost) {
        this.cost = cost;
    }
}
