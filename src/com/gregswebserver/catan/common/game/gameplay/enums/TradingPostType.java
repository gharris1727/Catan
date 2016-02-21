package com.gregswebserver.catan.common.game.gameplay.enums;

/**
 * Created by Greg on 8/13/2014.
 * Class containing information about the trades offered at a trading post.
 * Used in a HashMap where two locations point to the same object.
 */
public enum TradingPostType {

    Brick(GameResource.Brick),
    Lumber(GameResource.Lumber),
    Wool(GameResource.Wool),
    Grain(GameResource.Grain),
    Ore(GameResource.Ore),
    Wildcard(null);

    public final GameResource gameResource;

    TradingPostType(GameResource r) {
        gameResource = r;
    }

    @Override
    public String toString() {
        if (gameResource != null)
            return gameResource.toString();
        return "Any";
    }
}
