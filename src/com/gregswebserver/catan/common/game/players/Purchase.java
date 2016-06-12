package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

/**
 * Created by Greg on 8/13/2014.
 * Enum of purchase-able items for use in the trading framework.
 */
public enum Purchase {

    Road(new GameResource[]{GameResource.Brick, GameResource.Lumber}),
    Settlement(new GameResource[]{GameResource.Brick, GameResource.Lumber, GameResource.Grain, GameResource.Wool}),
    City(new GameResource[]{GameResource.Grain, GameResource.Grain, GameResource.Ore, GameResource.Ore, GameResource.Ore}),
    DevelopmentCard(new GameResource[]{GameResource.Wool, GameResource.Grain, GameResource.Ore});

    private final EnumCounter<GameResource> cost;

    Purchase(GameResource[] resources) {
        cost = new EnumCounter<>(GameResource.class);
        for (GameResource r : resources)
            cost.increment(r, 1);
    }

    public int get(GameResource r) {
        return cost.get(r);
    }

    //TODO: remove this in favor of an iterable way to access the data.
    public EnumCounter<GameResource> getCost() {
        return cost;
    }

}
